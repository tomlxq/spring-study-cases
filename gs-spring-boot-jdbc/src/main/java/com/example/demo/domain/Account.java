package com.example.demo.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @Transient
    Long id;
    @Column(name = "name")
    String name;
    Double money;
}

