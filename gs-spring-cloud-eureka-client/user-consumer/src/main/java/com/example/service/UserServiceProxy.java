package com.example.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.domain.User;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/27
 */
@Service
public class UserServiceProxy implements UserService {
    private static final String PROVIDER_SERVER_URL_PREFIX = "http://USER-SERVICE-PROVIDER";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean saveUser(User user) {
        User retValue = restTemplate.postForObject(PROVIDER_SERVER_URL_PREFIX + "/user/save", user, User.class);
        return retValue == null ? false : true;
    }

    @Override
    public Collection<User> findAll() {
        return restTemplate.getForObject(PROVIDER_SERVER_URL_PREFIX + "/user/list", Collection.class);
    }
}
