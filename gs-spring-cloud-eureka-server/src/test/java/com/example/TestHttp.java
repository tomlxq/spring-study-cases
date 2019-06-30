package com.example;

import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/30
 */
public class TestHttp {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        System.out.println(restTemplate.getForObject("http://localhost:6060/env", Map.class));
    }
}
