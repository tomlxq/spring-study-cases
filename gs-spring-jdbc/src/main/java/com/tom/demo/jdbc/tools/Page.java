package com.tom.demo.jdbc.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> implements Serializable {
    public static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * 当前面第一条数据在List中的位置
     */
    long start = 0;
    /**
     * 总记录数
     */
    long total;
    /**
     * 分页数
     */
    long pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 每页的记录数
     */
    List<T> row;
}
