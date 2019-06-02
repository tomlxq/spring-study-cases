package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.AppRootConfig;
import com.example.demo.dao.AccountDao;
import com.example.demo.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

@ContextConfiguration(classes = {AppRootConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJdbc {

    @Autowired
    AccountDao accountDao;

    //@Autowired  jdbcTemplate jdbcTemplate;
    @Test
    public void test2() {


        JdbcTemplate jdbcTemplate = accountDao.getJdbcTemplateWrite();
        String sql = "UPDATE account SET name=?, money=? "
                + " WHERE id=?";
        jdbcTemplate.update(sql, "jack", 288l, 1l);
        sql = "INSERT INTO account (name, money)"
                + " VALUES (?, ?)";
        jdbcTemplate.update(sql, "roze", 1000l);
    }

    @Test
    public void test3() {
        JdbcTemplate jdbcTemplate = accountDao.getJdbcTemplateWrite();

        String sql = "DELETE FROM account WHERE id=?";
        jdbcTemplate.update(sql, 1);

    }

    @Test
    public void test4() {
        JdbcTemplate jdbcTemplate = accountDao.getJdbcTemplateReadOnly();
        String sql = "SELECT * FROM account";
        List<Account> listContact = jdbcTemplate.query(sql, new RowMapper<Account>() {

            @Override
            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                Account aContact = new Account();

                aContact.setId(rs.getLong("id"));
                aContact.setName(rs.getString("name"));
                aContact.setMoney(rs.getDouble("money"));

                return aContact;
            }

        });
        System.out.println(JSON.toJSONString(listContact));
    }

    @Test
    public void test5() {
        JdbcTemplate jdbcTemplate = accountDao.getJdbcTemplateReadOnly();
        String sql = "SELECT * FROM account where id=2";
        Account account = jdbcTemplate.queryForObject(sql, new RowMapper<Account>() {


            public Account mapRow(ResultSet rs, int rowNum) throws SQLException,
                    DataAccessException {
                Account aContact = new Account();

                aContact.setId(rs.getLong("id"));
                aContact.setName(rs.getString("name"));
                aContact.setMoney(rs.getDouble("money"));

                return aContact;
            }

        });
        System.out.println(JSON.toJSONString(account));
    }


    @Test
    public void testAccount() {
        List<Account> list = accountDao.selectByName("tom");
        System.out.println(JSON.toJSONString(list));
    }


    @Test
    public void test() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "test", "test");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from account");
        ResultSet resultSet = preparedStatement.executeQuery();
        Class clazz = Account.class;
        while (resultSet.next()) {
            Object vo = clazz.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            IntStream.range(1, metaData.getColumnCount() + 1).forEach(idx -> {
                try {
                    String columnName = resultSet.getMetaData().getColumnName(idx);
                    Field declaredField = clazz.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    Class<?> type = declaredField.getType();

                    if (type == Long.class) {
                        declaredField.set(vo, resultSet.getLong(columnName));
                    } else if (type == Double.class) {
                        declaredField.set(vo, resultSet.getDouble(columnName));
                    } else if (type == String.class) {
                        declaredField.set(vo, resultSet.getString(columnName));
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            System.out.println(JSON.toJSONString(vo));
        }
        //Field[] fields = clazz.getDeclaredFields();

        //反之，利用反射机制可以获取到对象的属性名称以及属性的类型
        //动态提取到实体类的所有的字段


    }
}
