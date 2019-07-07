package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * {@link PersonProviderApp}服务端启动类
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/7/1
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
// @Import(WebConfig.class)
public class PersonProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonProviderApp.class, args);
    }

}
