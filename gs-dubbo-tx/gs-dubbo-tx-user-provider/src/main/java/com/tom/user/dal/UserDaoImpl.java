package com.tom.user.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements IUserDao {
    @Autowired
    JdbcTemplate orderJdbcTemplate;


    @Override
    public int recharge() {
        orderJdbcTemplate.execute("update user set name='jack',mobile='123456',sex='female' where id=1");
        return 0;
    }
}
