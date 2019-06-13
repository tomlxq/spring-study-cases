package com.example.controller;


import com.example.domain.Person;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @RestController=@Controller+@ResponseBody
 */
@RestController
public class PersonController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/npe")
    public String npe() throws NullPointerException {
        throw new NullPointerException();

    }

    /**
     * http://localhost:8080/person/1
     *
     * @param id
     * @param name
     * @return
     * @PostMapping @RequestMapping(method = RequestMethod.POST) create C
     * @GetMapping @RequestMapping(method = RequestMethod.GET) Read r
     * @PutMapping @RequestMapping(method = RequestMethod.PUT) Update u
     * @DeleteMapping @RequestMapping(method = RequestMethod.DELETE) Delete d
     * windows post
     * linux curl -X post
     */
    @GetMapping("/person/{id}")
    public Person getPerson(@PathVariable long id, @RequestParam(required = false) String name) {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        return person;
    }

    @GetMapping("/404.html")
    public Object errorPage(HttpStatus httpStatus, HttpServletRequest request, Throwable throwable) {
        Map<String, Object> map = Maps.newHashMap();

        map.put("status_code", request.getAttribute("javax.servlet.error.status_code"));
        map.put("exception_type", request.getAttribute("javax.servlet.error.exception_type"));
        map.put("message", request.getAttribute("javax.servlet.error.message"));
        map.put("exception", request.getAttribute("javax.servlet.error.exception"));
        map.put("request_uri", request.getAttribute("javax.servlet.error.request_uri"));
        map.put("servlet_name", request.getAttribute("javax.servlet.error.servlet_name"));
        return map;
    }
}
