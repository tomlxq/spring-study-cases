package com.example.controller;

import com.example.dao.PersonMapper;
import com.example.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableTransactionManagement  // 需要事务的时候加上
@RestController
public class PersonController {

    @Autowired
    private PersonMapper personMapper;

    @RequestMapping("/save")
    public Integer save() {
        Person person = new Person();
        person.setName("张三");
        person.setAge(18);
        personMapper.insert(person);
        return person.getId();
    }

    @RequestMapping("/update")
    public Long update() {
        Person person = new Person();
        person.setId(2);
        person.setName("旺旺");
        person.setAge(12);
        return personMapper.update(person);
    }

    @RequestMapping("/delete")
    public Long delete() {
        return personMapper.delete(11L);
    }

    @RequestMapping("/selectById")
    public Person selectById() {
        return personMapper.selectById(2L);
    }

    @RequestMapping("/selectAll")
    public List<Person> selectAll() {
        return personMapper.selectAll();
    }

    @RequestMapping("/transaction")
    @Transactional  // 需要事务的时候加上
    public Boolean transaction() {
        delete();

        int i = 3 / 0;

        save();

        return true;
    }

}