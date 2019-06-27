package com.example.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.User;
import com.example.service.UserService;

/**
 * {@link UserService 用户服务} 提供 REST API {@link RestController}
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/26
 */
@RestController
public class UserProviderController {
    @Autowired
    private UserService userService;

    /**
     * saveUser
     *
     * @param user
     * @return 如果成功返回{@link User} 否则返回<code>null</code>
     */
    @PostMapping("/user/save")
    public User saveUser(@RequestBody User user) {
        if (userService.saveUser(user)) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * getUsers
     *
     * @return 如果成功返回{@link Collection<User></User>}
     */
    @GetMapping("/user/list")
    public Collection<User> getUsers() {
        return userService.findAll();
    }
}
