package com.example.demo.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "hotel")
public class Hotel implements Serializable {
    @Id
    String id;
    String name;
    String addr;
    float price;
    Date createTime;
}
