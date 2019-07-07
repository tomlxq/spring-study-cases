package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
@EnableDiscoveryClient
public class GsSpringCloudZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudZipkinApplication.class, args);
    }

}
