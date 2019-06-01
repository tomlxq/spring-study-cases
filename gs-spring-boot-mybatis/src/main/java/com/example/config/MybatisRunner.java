package com.example.config;

import com.example.dao.PersonMapper;
import com.example.dao.UserMapper;
import com.example.domain.Person;
import com.example.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MybatisRunner implements CommandLineRunner {

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private UserMapper userMapper;
    @Override
    public void run(String... args) throws Exception {
        Person person = new Person();
        person.setAge(19);
        person.setName("tom");
        personMapper.insert(person);
        List<Person> people = personMapper.selectAll();
        System.out.println(people);
        Person person1 = people.get(0);
        person1.setName("jack");
        person1.setAge(100);
        personMapper.update(person1);
        people = personMapper.selectAll();
        System.out.println(people);
        people.forEach(p -> {
            personMapper.delete(p.getId());
        });

        User user = new User();
        user.setUsername("tom");
        user.setAge(19);
        user.setPassword("tom");
        userMapper.insert(user);


    }
}
