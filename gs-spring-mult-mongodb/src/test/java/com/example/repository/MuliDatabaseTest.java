package com.example.repository;
import com.alibaba.fastjson.JSON;
import com.example.domain.User;
import com.example.repository.primary.PrimaryRepository;
import com.example.repository.secondary.SecondaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MuliDatabaseTest {

    @Autowired
    private PrimaryRepository primaryRepository;

    @Autowired
    private SecondaryRepository secondaryRepository;

    @Test
    public void TestSave() {
        LOGGER.info("************************************************************");
        LOGGER.info("测试开始");
        LOGGER.info("************************************************************");
        this.primaryRepository.save(new User("1","小张", "123456"));
        this.secondaryRepository.save(new User("2","小王", "654321"));
        List<User> primaries = this.primaryRepository.findAll();
        for (User primary : primaries) {
            LOGGER.info("{}", JSON.toJSONString(primary,true));
        }
        List<User> secondaries = this.secondaryRepository.findAll();
        for (User secondary : secondaries) {
            LOGGER.info("{}",JSON.toJSONString(secondary,true));
        }
        LOGGER.info("************************************************************");
        LOGGER.info("测试完成");
        LOGGER.info("************************************************************");
    }

}
