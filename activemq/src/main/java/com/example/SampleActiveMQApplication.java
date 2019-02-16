package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/*
@SpringBootApplication
public class ActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivemqApplication.class, args);
    }
}
*/


/*
@Configuration//配置控制
@EnableAutoConfiguration//启用自动配置
@ComponentScan//组件扫描
@EnableConfigurationProperties({ActivemqApplication.class})
@EnableJms
public class ActivemqApplication {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ActivemqApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ActivemqApplication.class, args);
        LOGGER.spring("Server running...");
    }

}*/
import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SampleActiveMQApplication {

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("sample.queue");
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleActiveMQApplication.class, args);
    }

}