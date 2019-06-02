package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.domain.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    /**
     * 测试插入
     *
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public String add() {
        User u = new User();
        int i = (int) Math.random() * 100;
        //  u.setId(i);
        u.setUsername("test" + i);
        u.setPassword(UUID.randomUUID().toString());
        u.setAge(i);
        this.userService.insertUser(u);
        return "success";
    }

    /**
     * 测试分页插件
     *
     * @return
     */
    @RequestMapping("/queryPage")
    @ResponseBody
    public String queryPage() {
        PageInfo<User> page = this.userService.queryPage("tes", 1, 2);
        log.info("{}", JSON.toJSONString(page, true));

        return "success";
    }

    /**
     * 测试事务
     *
     * @return
     */
    @RequestMapping("/testTransational")
    @ResponseBody
    public String test() {
        try {
            this.userService.testTransational();
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
