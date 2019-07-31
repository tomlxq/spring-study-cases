package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/29
 */
@Configuration
@ComponentScan("com.example")
public class SpringWebmvcConfig {
    // @ConditionalOnClass(String.class)
    @Bean("helloWorld")
    public String helloWorld() {
        return "Hello,world";
    }
}
