package com.foo.entity;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @ClassName: Student
 * @Description:
 * @Author: tomluo
 * @Date: 2022/12/18 14:54
 **/
@Data
@Builder
public class Student {
    private int id;
    private String name;
    private int age;
}
