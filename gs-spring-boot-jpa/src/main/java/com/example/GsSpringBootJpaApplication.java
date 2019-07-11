package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class GsSpringBootJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsSpringBootJpaApplication.class, args);
    }

}
