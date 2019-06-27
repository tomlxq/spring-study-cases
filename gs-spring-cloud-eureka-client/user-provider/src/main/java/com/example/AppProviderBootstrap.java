package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/26
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AppProviderBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AppProviderBootstrap.class, args);
    }
}
