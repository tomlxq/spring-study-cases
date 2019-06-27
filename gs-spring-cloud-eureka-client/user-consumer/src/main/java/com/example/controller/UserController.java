package com.example.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.User;
import com.example.service.UserServiceProxy;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/26
 */
@RestController
public class UserController {
    @Autowired
    private UserServiceProxy userServiceProxy;

    /**
     * saveUser
     * 
     * @param user
     * @return 如果成功返回{@link User} 否则返回<code>null</code>
     */
    @PostMapping("/user/save")
    public boolean saveUser(@RequestBody User user) {
        return userServiceProxy.saveUser(user);
    }

    /**
     * getUsers
     * 
     * @return 如果成功返回{@link Collection<User></User>}
     */
    @GetMapping("/user/list")
    public Collection<User> getUsers() {
        return userServiceProxy.findAll();
    }

}
