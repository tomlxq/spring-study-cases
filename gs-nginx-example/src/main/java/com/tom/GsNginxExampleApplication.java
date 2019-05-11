package com.tom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class GsNginxExampleApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GsNginxExampleApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GsNginxExampleApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        // 目的是
        return args -> {

            LOGGER.info("来看看 SpringBoot 默认为我们提供的 Bean：");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            Arrays.stream(beanNames).forEach(System.out::println);
        };
    }
}
