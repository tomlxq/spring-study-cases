package com.example;

import com.alibaba.fastjson.JSON;
import com.example.domain.Student;
import com.example.service.StudentService;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
//@CommonsLog
@SpringBootTest
@Slf4j
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;

    @Test
    public void testFindByPage() {
        Page<Student> students = studentService.findByPage(1, 2);//Query pageNo=1, pageSize=2
        assertEquals(students.getTotal(),11);
        assertEquals(students.getPages(),5);
        log.debug("{}", JSON.toJSONString(students,true));
    }
}