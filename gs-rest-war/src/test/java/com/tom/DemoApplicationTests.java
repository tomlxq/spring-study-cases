package com.tom;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class DemoApplicationTests {
    public final static Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);
    @Autowired
    UserDao userDao;

    @Test
    public void contextLoads() {
        userDao.save(new User("tom", "tom@.com"));
        User user = userDao.findByEmail("tom@.com");
        logger.debug("{}", user);
    }

}

