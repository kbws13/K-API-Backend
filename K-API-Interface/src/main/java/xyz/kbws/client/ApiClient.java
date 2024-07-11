package xyz.kbws.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import xyz.kbws.model.User;

import java.util.HashMap;

/**
 * @author kbws
 * @date 2024/7/11
 * @description: 调用第三方接口的客户端
 */
public class ApiClient {

    public String getNameByGet(String name) {
        HashMap<String, Object> parmaMap = new HashMap<>();
        parmaMap.put("name", name);
        String result = HttpUtil.get("http://localhost:8123/api/name/", parmaMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> parmaMap = new HashMap<>();
        parmaMap.put("name", name);
        String result = HttpUtil.post("http://localhost:8123/api/name/", parmaMap);
        System.out.println(result);
        return result;
    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/user")
                .body(json)
                .execute();
        return httpResponse.body();
    }
}
