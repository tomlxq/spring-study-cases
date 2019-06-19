package com.example.config;

import java.util.Collections;
import java.util.Map;

import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * 功能描述
 *
 * @author hp
 * @email 72719046@qq.com
 * @date 2019/6/19
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppConfig implements PropertySourceLocator {

    @Override
    public PropertySource<?> locate(Environment environment) {
        Map<String, Object> stringObjectMap = Collections.<String, Object>singletonMap("name", "tomLuo");

        // stringObjectMap.put("age", "18");
        return new MapPropertySource("testProperty", stringObjectMap);
    }
}
