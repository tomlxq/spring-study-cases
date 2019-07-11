package com.example.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Person实体
 *
 * @author TomLuo
 * @date 2019/7/9
 */
@Entity
@Table(name = "person")
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(columnDefinition = "VARCHAR(128) NOT NULL")
    String name;

    int age;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;

}
