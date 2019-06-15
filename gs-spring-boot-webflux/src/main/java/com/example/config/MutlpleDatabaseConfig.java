package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class MutlpleDatabaseConfig {
    @Value("${spring.datasource.driverClassName}")
    String driverClassName;

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.url2}")
    String url2;

    @Bean
    @Primary
    public DataSource masterDataSource() {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        DataSource dataSource = dataSourceBuilder.driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
        return dataSource;
    }

    @Bean
    public DataSource slaveDataSource() {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        DataSource dataSource = dataSourceBuilder.driverClassName(driverClassName)
                .url(url2)
                .username(username)
                .password(password)
                .build();
        return dataSource;
    }
}
