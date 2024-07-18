package xyz.kbws.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author kbws
 * @date 2024/7/18
 * @description:
 */
@DubboService
public class DemoServiceImpl implements DemoService{
    @Override
    public String sayHello(String name) {
        System.out.println("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name;
    }

    @Override
    public String sayHello2(String name) {
        return "kbws";
    }

}
