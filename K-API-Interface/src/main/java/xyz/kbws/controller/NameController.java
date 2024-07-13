package xyz.kbws.controller;

import org.springframework.web.bind.annotation.*;
import xyz.kbws.model.User;
import xyz.kbws.utils.SignUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kbws
 * @date 2024/7/11
 * @description:
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/")
    public String getNameByGet(String name) {
        return "GET 你的名字是 " + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是 " + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        // TODO 去数据库查询
        if (!accessKey.equals("kbws")) {
            throw new RuntimeException("无权限");
        }
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        // TODO 校验时间戳是否超出一定时间
        String serverSign = SignUtil.getSign(body, "Eternal");
        if (!serverSign.equals(sign)) {
            throw new RuntimeException("无权限");
        }
        return "POST 你的名字是 " + user.getUserName();
    }
}
