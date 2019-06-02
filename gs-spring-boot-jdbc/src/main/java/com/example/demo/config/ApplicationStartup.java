package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/*
 * 监听springboot启动类是否启动，启动就执行数据操作
 * */
@Configuration
@Slf4j
public class ApplicationStartup implements ApplicationRunner {
    //@Override
   // public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
      //  LOGGER.info("系统启动时，作初始化的动作");
   // }

    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("系统启动时，作初始化的动作");
    }
}
