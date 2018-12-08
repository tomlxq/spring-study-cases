package com.tom.demo.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * 使用此配置类替换spring-configs.xml
 *
 * @Configuration 描述这个类是个配置类
 * @ComponentScan 用于定义要扫描的具体包
 */

@PropertySource("classpath:jdbc.properties")
@ComponentScan(basePackages = {"com.tom.demo.entity"})
public class AppRootConfig {


    /**
     * 整合第三方bean
     *
     * @return
     */
    @Bean("dataSource")//假如没有指定名字，默认为方法名
    @Lazy(false)
    public DataSource newDataSource(@Value("${jdbc.driverClassName}") String driver,
                                    @Value("${jdbc.url}") String url,
                                    @Value("${jdbc.username}") String user,
                                    @Value("${jdbc.password}") String password) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        return ds;
    }
}


