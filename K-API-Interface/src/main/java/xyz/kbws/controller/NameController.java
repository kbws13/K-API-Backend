package xyz.kbws.controller;

import org.springframework.web.bind.annotation.*;
import xyz.kbws.model.User;

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
    public String getUserNameByPost(@RequestBody User user) {
        return "POST 你的名字是 " + user.getUserName();
    }
}
