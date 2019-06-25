package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 * 功能描述
 *
 * @author hp
 * @email 72719046@qq.com
 * @date 2019/6/19
 */
@RestController
@RefreshScope
public class EchoEnvController {
    public final Environment environment;

    @Autowired
    public EchoEnvController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/get/env")
    public String getEnv() {
        return JSON.toJSONString(environment, true);
    }

    @Value("${my.name}")
    private String myName;

    @GetMapping("/get/myName")
    public String getMyName() {
        return myName;
    }
}
