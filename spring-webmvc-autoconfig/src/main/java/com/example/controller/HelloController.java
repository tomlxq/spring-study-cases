package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/29
 */
@RestController
public class HelloController {
    @Autowired(required = false)
    @Qualifier("helloWorld")
    private String helloWorld;

    @GetMapping
    public String hello() {
        return helloWorld;
    }
}
