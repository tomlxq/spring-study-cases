package com.example.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.Person;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/10
 */
@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Integer> {

}
