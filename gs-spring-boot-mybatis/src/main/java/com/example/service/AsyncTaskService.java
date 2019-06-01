package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * https://www.bswen.com/2018/04/springboot-Setup-ExecutorService-with-springboot.html
 */
@Component
public class AsyncTaskService {

    @Autowired
    @Qualifier("fixedThreadPool")
    private ExecutorService executorService;

    public <T> Future<T> executeWithResult(Callable<T> callable) {
        return executorService.submit(callable);
    }
}
