package com.example.domain;

import com.github.pagehelper.Page;
import lombok.Data;

@Data
public class DataTableResultInfo {
    private Page<Student> data;
    private int draw;//the NO.of requests
    private int length;
    private long recordsTotal;
    private long recordsFiltered;
}
