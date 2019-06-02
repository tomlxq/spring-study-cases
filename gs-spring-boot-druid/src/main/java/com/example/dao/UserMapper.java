package com.example.dao;

import com.example.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert t_user(username,password,age) values(#{username},#{password},#{age})")
    void insert(User u);

    //注：方法名和要UserMapper.xml中的id一致
    List<User> query(@Param("username") String username);

    @Delete("delete from t_user")
    void deleteAll();
}
