package com.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.example.config.TomcatConfiguration;

@SpringBootApplication
public class SpringBootEmbeddedTomcatApplication {

    public static void main(String[] args) {
        // SpringApplication.run(TomcatSpringBootWebApplication.class, args);
        new SpringApplicationBuilder().sources(SpringBootEmbeddedTomcatApplication.class, TomcatConfiguration.class)
            .run(args);
    }

}
