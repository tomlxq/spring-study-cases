package com.example.boostrap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.example.domain.User;

/**
 * {@link XmlConfigBootstrap} 构建一个spring application context
 *
 * @author TomLuo
 * @date 2019/7/28
 */
public class XmlConfigBootstrap {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("classpath:/META-INFO/spring/context.xml");
        context.refresh();
        User user = context.getBean("user", User.class);
        System.out.println(JSON.toJSONString(user, true));
    }
}
