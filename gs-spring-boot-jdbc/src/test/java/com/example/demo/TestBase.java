package com.example.demo;

import com.example.demo.config.AppRootConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class TestBase {
    protected AnnotationConfigApplicationContext ctx;

    @Before
    public void init() {
        ctx = new AnnotationConfigApplicationContext(AppRootConfig.class);
    }

    @After
    public void close() {
        ctx.close();
    }

    @Test
    public void testDruidDataSource() throws SQLException {
        DataSource ds = ctx.getBean("dataSource", DataSource.class);
        System.out.println(ds);
        System.out.println(ds.getClass().getName());
        Connection conn = ds.getConnection();
        System.out.println(conn);
        conn.close();
    }


}

