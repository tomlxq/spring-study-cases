package com.example.task;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author hp
 * @email 72719046@qq.com
 * @date 2019/6/25
 */
@Component
@EnableScheduling
@Slf4j

public class SchedulerTask {
    private final ContextRefresher contextRefresher;
    private final Environment environment;

    @Autowired
    public SchedulerTask(ContextRefresher contextRefresher, Environment environment) {
        this.contextRefresher = contextRefresher;
        this.environment = environment;
    }

    @Scheduled(fixedRate = 5 * 1000, initialDelay = 1 * 1000)
    public void autoRefresh() {
        Set<String> refresh = contextRefresher.refresh();
        refresh.forEach(name -> log.info("thread [{}],当前变更的项为key :{} value: {}", Thread.currentThread().getName(), name,
            environment.getProperty(name)));
        if (!refresh.isEmpty()) {
            log.info("thread [{}],当前变更的项为{}", Thread.currentThread().getName(), refresh);
        }

    }
}
