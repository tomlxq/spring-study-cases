package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;

import com.example.config.MyHealthIndicator;

@SpringBootApplication
@EnableConfigServer
public class GsSpringCloudServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudServerApplication.class, args);
    }

    @Bean
    MyHealthIndicator myHealthIndicator() {
        return new MyHealthIndicator();
    }
}
