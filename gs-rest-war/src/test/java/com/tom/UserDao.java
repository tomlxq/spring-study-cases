package com.tom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tom on 2016/5/2.
 */
@Transactional
public interface UserDao extends CrudRepository<User, Long> {
    public User findByEmail(String email);
}
