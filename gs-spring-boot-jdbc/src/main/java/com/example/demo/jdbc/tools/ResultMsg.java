package com.example.demo.jdbc.tools;


import lombok.Data;

import java.io.Serializable;

@Data
public class ResultMsg<T> implements Serializable {
    /**
     * 状态码
     */
    int status;
    /**
     * 消息体
     */
    String msg;
    /**
     * 数据
     */
    T data;
}
