package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.example.ribbon.FirstServerForeverServer;
import com.example.service.PersonService;

/**
 * {@link PersonClientApp}客户端记动类
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/30
 */
@SpringBootApplication
@EnableFeignClients(clients = PersonService.class)
@EnableEurekaClient
@EnableHystrix
// @Import(WebConfig.class)
// @RibbonClient(value = "person-provider", configuration = PersonClientApp.class)
public class PersonClientApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonClientApp.class, args);
    }

    /**
     * 暴露{@link FirstServerForeverServer}
     *
     * @return
     */
    @Bean
    FirstServerForeverServer firstServerForeverServer() {
        return new FirstServerForeverServer();
    }
}
