package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class WebFluxConfig {
    @Bean
    public RouterFunction<ServerResponse> saveUser(UserHandler userHandler) {
        log.info("thread: {} flux ", Thread.currentThread().getName());
        return route(POST("/flux/user/save"), userHandler::save);

    }
}
