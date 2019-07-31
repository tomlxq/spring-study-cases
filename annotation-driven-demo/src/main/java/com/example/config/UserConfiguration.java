package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.domain.User;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/28
 */
@Configuration
public class UserConfiguration {
    @Bean(name = "user")
    public User user() {
        User user = new User();
        user.setName("tom");
        return user;
    }
}
