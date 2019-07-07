package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/7
 */
@RestController
@Slf4j
public class DemoController {
    @Autowired
    public DemoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;

    @GetMapping("/demo")
    public String index() {
        String message = "Hello, Tom";
        log.info("{} message {}", getClass().getName(), message);
        return message;
    }

    /**
     * http://localhost:20000/zuul/person-client/person/findAll
     * sleuth(20000)->zuul(6060)->person-client()->person-provider
     * 
     * @return
     */
    @GetMapping("/zuul/person-client/person/findAll")
    public Object toZuul() {
        log.info("DemoController#toZuul()");
        String url = "http://spring-cloud-zuul/person-client/person/findAll";
        return restTemplate.getForObject(url, Object.class);
    }

}
