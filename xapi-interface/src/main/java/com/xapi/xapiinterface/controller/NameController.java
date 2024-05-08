package com.xapi.xapiinterface.controller;

import com.xapi.xapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author 15304
 * 模拟API
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是 " + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "POST 你的名字是 " + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user){
        // 鉴权由网关统一实现
        return "用户名字是 " + user.getUsername();
    }

}
