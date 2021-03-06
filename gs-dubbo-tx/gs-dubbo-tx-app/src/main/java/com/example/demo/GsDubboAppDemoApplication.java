package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath*:META-INF/client/order-consumer.xml", "classpath*:META-INF/client/user-consumer.xml"})
public class GsDubboAppDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsDubboAppDemoApplication.class, args);
    }

}

