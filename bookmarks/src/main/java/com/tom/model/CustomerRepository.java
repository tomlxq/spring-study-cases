package com.tom.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by tom on 2016/5/9.
 */
public  interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}