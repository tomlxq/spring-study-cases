package com.example.config;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

/**
 * 功能描述
 *
 * @author hp
 * @email 72719046@qq.com
 * @date 2019/6/26
 */
public class MyHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up().withDetail("UP", "DAY DAY UP");
        builder.build();
    }
}
