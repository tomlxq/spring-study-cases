package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.domain.Person;

/**
 * {@link Person}服务
 *
 * @author TomLuo
 * @date 2019/7/9
 */
@Service
@Transactional
public class PersonService {
    /**
     * 通过标准的ＪＰＡ方式注入
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@link Person}的存储
     * 
     * @param person
     */
    public void save(Person person) {
        entityManager.persist(person);
    }

    public List<Person> findAll() {
        return (List<Person>)entityManager.createQuery("select id,name,age,create_time as createTime from person",
            Person.class);
    }
}
