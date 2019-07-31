package com.example.boostrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.alibaba.fastjson.JSON;
import com.example.config.UserConfiguration;
import com.example.domain.User;

/**
 * {@link AnnotationConfigApplicationContext} 构建一个spring application context
 *
 * @author TomLuo
 * @date 2019/7/29
 */
public class AnnotationConfigBootstrap {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserConfiguration.class);
        context.refresh();
        User user = context.getBean("user", User.class);
        System.out.println(JSON.toJSONString(user, true));
    }
}
