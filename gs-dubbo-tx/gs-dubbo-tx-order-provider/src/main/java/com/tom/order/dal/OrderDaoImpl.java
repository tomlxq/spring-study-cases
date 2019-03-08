package com.tom.order.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired
    JdbcTemplate orderJdbcTemplate;

    @Override
    public int insertOrder() {
        orderJdbcTemplate.execute("insert into order(status,price,create_time,modify_time)values (1,1.00,now(),now())");
        return 0;
    }
}
