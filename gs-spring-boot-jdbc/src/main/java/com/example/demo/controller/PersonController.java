package com.example.demo.controller;


import com.example.demo.domain.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class PersonController {
    /**
     * http://localhost:8080/person/1
     * @param id
     * @param name
     * @return
     */
    @GetMapping("/person/{id}")
    public Person getPerson(@PathVariable long id, @RequestParam(required = false) String name){
        Person person=new Person();
        person.setId(id);
        person.setName(name);
        return person;
    }
}
