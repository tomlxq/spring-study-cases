package com.example.dao;

import com.example.domain.Student;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {
    Page<Student> findByPage();
}
