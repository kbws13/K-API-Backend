package xyz.kbws.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.kbws.model.User;

/**
 * @author kbws
 * @date 2024/7/11
 * @description:
 */
@Slf4j
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
    public String getUserNameByPost(@RequestBody User user) {
        log.info("接受到请求：{}", user.getUserName());
        return "POST 你的名字是 " + user.getUserName();
    }
}
