package com.example.service;

import com.example.dao.StudentMapper;
import com.example.domain.Student;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public Page<Student> findByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize); //line 1
        return studentMapper.findByPage(); //line 2
    }

}