package com.example.repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.domain.User;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/26
 */
@Repository
public class UserRepository {
    private static final ConcurrentMap<Long, User> userRepository = new ConcurrentHashMap<>();

    private AtomicLong idGenerator = new AtomicLong(0);

    public Collection<User> findAll() {
        return userRepository.values();
    }

    public boolean save(User user) {
        long id = idGenerator.incrementAndGet();
        user.setId(id);
        return userRepository.putIfAbsent(id, user) == null;
    }
}
