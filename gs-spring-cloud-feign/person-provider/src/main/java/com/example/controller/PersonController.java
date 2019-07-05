package com.example.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Person;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/7/1
 */
@RestController
@Slf4j
public class PersonController {
    private final Map<Long, Person> map = new ConcurrentHashMap();

    @PostMapping("/person/save")
    boolean savePerson(@RequestBody Person person) {
        return map.put(person.getId(), person) == null;
    }

    private final Random random = new Random();

    /**
     * 超过100ms服务熔断
     * 
     * @return
     */
    @GetMapping("/person/findAll")
    @HystrixCommand(fallbackMethod = "fallbackFindAll", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")})

    Collection<Person> findAll() throws InterruptedException {
        int time = random.nextInt(200);
        log.info("findAll cost {} ms。 {}", time, Thread.currentThread().getName());
        Thread.sleep(time);
        return map.values();
    }

    Collection<Person> fallbackFindAll() {
        log.error("发生服务熔断，调到fallbackFindAll. {}", Thread.currentThread().getName());
        return Collections.emptyList();
    }
}
