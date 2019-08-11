package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/8/2
 */
@RestController
public class IndexController {
    @GetMapping
    public String index() {
        return "Hello,强哥";
    }
}
