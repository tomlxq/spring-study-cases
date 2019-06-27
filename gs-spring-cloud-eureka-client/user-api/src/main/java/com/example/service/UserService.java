package com.example.service;

import java.util.Collection;

import com.example.domain.User;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/26
 */
public interface UserService {
    /**
     * 保存用户
     * 
     * @param user
     * @return
     */
    boolean saveUser(User user);

    /**
     * 查询所有用户
     * 
     * @return
     */
    Collection<User> findAll();
}
