package com.example.demo;

import com.example.demo.dao.StudentDao;
import com.example.demo.domain.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDefaultDataSource {
    @Autowired
    private StudentDao studentDao;
    @Test
    public void testSimpleSelect() {
        Student student = studentDao.getStudent(1);
        assertNotNull(student);
        assertEquals(student.getName(), "jack");
    }
}