
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GsSpringCloudClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GsSpringCloudClientApplication.class);
        // application.setBanner(Banner.Mode.OFF);
        application.run(args);
    }

}
