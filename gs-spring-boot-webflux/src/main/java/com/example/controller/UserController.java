package com.example.controller;

import com.example.domain.User;
import com.example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@Slf4j
public class UserController {
    private final UserRepository userRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/web/user/save")
    public User save(@RequestBody User user) {
        log.info("thread: {} web ", Thread.currentThread().getName());
        userRepository.save(user);
        return user;
    }

    @RequestMapping("/web/user/save2")
    public int save2(@RequestBody User user) throws ExecutionException, InterruptedException {

        Future<Integer> submit = executorService.submit(() -> {
            return userRepository.save(user);
        });

        return submit.get();
    }
}
