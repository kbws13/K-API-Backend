package xyz.kbws.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import xyz.kbws.model.User;
import xyz.kbws.utils.SignUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kbws
 * @date 2024/7/11
 * @description: 调用第三方接口的客户端
 */
public class KApiClient {

    private String accessKey;
    private String secretKey;

    public KApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        HashMap<String, Object> parmaMap = new HashMap<>();
        parmaMap.put("name", name);
        String result = HttpUtil.get("http://localhost:8090/api/name/", parmaMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> parmaMap = new HashMap<>();
        parmaMap.put("name", name);
        String result = HttpUtil.post("http://localhost:8090/api/name/", parmaMap);
        System.out.println(result);
        return result;
    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8090/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        return httpResponse.body();
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        //map.put("secretKey", secretKey);
        map.put("body", body);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", SignUtil.getSign(body, secretKey));
        return map;
    }

}
