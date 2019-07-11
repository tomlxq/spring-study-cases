package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Person;
import com.example.repository.PersonRepository;
import com.example.service.PersonService;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/10
 */
@RestController
public class PersonController {
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/person/save")
    public Person save(@RequestBody Person person) {
        personService.save(person);
        return person;
    }

    @PostMapping("/person/save2")
    public Person save2(@RequestBody Person person) {
        personRepository.save(person);
        return person;
    }

    @GetMapping("/person/findAll")
    public Iterable<Person> findAll() {
        Iterable<Person> all = personRepository.findAll();
        return all;

    }

    @GetMapping("/person/findPage")
    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }
}
