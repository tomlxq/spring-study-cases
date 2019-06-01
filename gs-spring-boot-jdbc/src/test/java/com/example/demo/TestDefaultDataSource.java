package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.dao.StudentDao;
import com.example.demo.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestDefaultDataSource {
    @Autowired
    private StudentDao studentDao;

    @Test
    public void testSimpleSelect() {
        Student student = studentDao.getStudent(1);
        assertNotNull(student);
        LOGGER.info("{}", JSON.toJSONString(student, true));
        assertEquals(student.getName(), "jack");
    }
}
