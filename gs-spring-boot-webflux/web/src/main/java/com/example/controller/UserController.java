package com.example.controller;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.User;
import com.example.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {
    private final UserRepository userRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * 加 @Autowired表明通过构造函数注入
     * 
     * @param userRepository
     */
    @Autowired
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

    @PostMapping("/web/user/save3")
    public User save3(@RequestParam String name) {
        User user = new User();
        user.setName(name);
        userRepository.save3(user);
        return user;
    }

    /**
     * * @return the collection view 不能修改
     *
     * @return
     */
    @RequestMapping("/web/users")
    public Collection<User> findAll() {
        return userRepository.findAll();
    }
}
