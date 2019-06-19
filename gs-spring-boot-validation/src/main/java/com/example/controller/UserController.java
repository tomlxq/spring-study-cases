package com.example.controller;

import javax.validation.Valid;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.User;

@RestController
public class UserController {
    @PostMapping("/user/save1")
    public User saveUser(@Valid @RequestBody User user) {
        return user;
    }

    /**
     * 耦合高
     * 
     * @param user
     * @return
     */
    @PostMapping("/user/save2")
    public User saveUser2(@RequestBody User user) {
        // API 调用的方式
        Assert.notNull(user.getName(), "用户名不能为空");
        // JVM断言的方式
        assert user.getId() <= 10000;
        return user;
    }
}
