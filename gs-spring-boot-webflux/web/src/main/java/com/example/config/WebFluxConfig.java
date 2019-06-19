package com.example.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.domain.User;
import com.example.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Configuration
@Slf4j
public class WebFluxConfig {
    @Bean
    public RouterFunction<ServerResponse> saveUser(UserHandler userHandler) {
        log.info("thread: {} flux ", Thread.currentThread().getName());
        return route(POST("/flux/user/save"), userHandler::save);

    }

    @Bean
    @Autowired
    public RouterFunction<ServerResponse> routerFunctionUsers(UserRepository userRepository) {
        log.info("thread: {} flux ", Thread.currentThread().getName());
        Collection<User> all = userRepository.findAll();
        Flux<User> userFlux = Flux.fromIterable(all);
        return route(path("/users"), request -> ServerResponse.ok().body(userFlux, User.class));

    }
}
