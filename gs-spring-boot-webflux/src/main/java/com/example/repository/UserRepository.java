package com.example.repository;

import com.alibaba.fastjson.JSON;
import com.example.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

@Repository
@Slf4j
public class UserRepository {
    private final DataSource dataSource;
    private final DataSource masterDataSource;
    private final DataSource slaveDataSource;
    private final JdbcTemplate JdbcTemplate;

    public UserRepository(DataSource dataSource, DataSource masterDataSource, DataSource slaveDataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.masterDataSource = masterDataSource;
        this.slaveDataSource = slaveDataSource;
        JdbcTemplate = jdbcTemplate;
    }

    public int saveByJDBC(User user) {
        log.info("thread: {} save {}", Thread.currentThread().getName(), JSON.toJSONString(user, true));
        int count = -1;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into t_user(name) values(?)");
            preparedStatement.setString(1, user.getName());
            count = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {

                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }

    @Transactional
    public int save(User user) {
        log.info("thread: {} save {}", Thread.currentThread().getName(), JSON.toJSONString(user, true));
        JdbcTemplate.execute("insert into t_user(name) values(?)", new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, user.getName());
                return preparedStatement.executeUpdate();
            }
        });
        return -1;
    }

    public Collection<User> getUsers(User user) {
        log.info("{} ", Thread.currentThread().getName());
        return Collections.EMPTY_LIST;
    }
}
