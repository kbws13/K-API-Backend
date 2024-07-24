package xyz.kbws.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.kbws.model.entity.InterfaceInfo;
import xyz.kbws.model.vo.UserKey;
import xyz.kbws.service.InnerInterfaceInfoService;
import xyz.kbws.service.InnerUserInterfaceInfoService;
import xyz.kbws.service.InnerUserService;
import xyz.kbws.utils.SignUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author kbws
 * @date 2024/7/17
 * @description: 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    public static final String HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().value();
        path = HOST + path;
        String method = request.getMethod().toString();
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求唯一标识: {}, 请求路径: {}, 请求方法: {}, 请求参数: {}, 请求来源地址: {}, 请求目的地址: {}",
                request.getId(), path, method, request.getQueryParams(),
                sourceAddress, request.getRemoteAddress());
        // 2.黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        // 3.用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        UserKey invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error.", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        Long currentTime = System.currentTimeMillis() / 1000;
        Long FIVE_MIN = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MIN) {
            return handleNoAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtil.getSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // 4. 请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error.", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        // 5. 请求转发，调用模拟接口
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 7. 调用成功，接口调用次数 + 1
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                    log.info("接口调用次数增加成功: interfaceInfoId = {}, userId = {}", interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error.", e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                String data = new String(content, StandardCharsets.UTF_8);
                                log.info("响应结果：" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

}
