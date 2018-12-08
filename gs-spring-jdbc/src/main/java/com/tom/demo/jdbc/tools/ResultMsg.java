package com.tom.demo.jdbc.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
