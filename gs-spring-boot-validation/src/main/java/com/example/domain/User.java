package com.example.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.example.validation.constraints.InvalidMobile;

import lombok.Data;

@Data
public class User {
    @Max(value = 10000, message = "输入的id超出来了范围")
    int id;
    @NotNull
    String name;
    @Email
    String email;
    @NotNull
    @InvalidMobile
    String mobile;
}
