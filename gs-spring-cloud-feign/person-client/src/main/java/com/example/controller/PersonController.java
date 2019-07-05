package com.example.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Person;
import com.example.service.PersonService;

/**
 * {@link PersonController} 实现 {@link PersonService}
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/30
 */
@RestController
public class PersonController implements PersonService {
    public final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean savePerson(Person person) {
        return personService.savePerson(person);
    }

    @Override
    public Collection<Person> findAll() {
        return personService.findAll();
    }
}
