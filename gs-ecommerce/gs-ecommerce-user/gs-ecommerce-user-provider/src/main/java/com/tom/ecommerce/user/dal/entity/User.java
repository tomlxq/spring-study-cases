package com.tom.ecommerce.user.dal.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class User {

    private Integer id;

    private String username;

    private String password;

    private String realname;

    private String avatar;

    private String mobile;

    private String sex;

    private Integer status;

    private Date createTime;
}