package com.tom.demo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @Transient
    Long id;
    @Column(name = "name")
    String name;
    Double money;
}

