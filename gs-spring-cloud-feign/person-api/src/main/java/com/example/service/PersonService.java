package com.example.service;

import java.util.Collection;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.domain.Person;
import com.example.failback.PersonProviderFailBack;

/**
 * {@link Person} 服务调用
 * 
 * @FeignClient(values="xxx")为服务提供方的应用名称
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/30
 */
@FeignClient(value = "person-provider", fallback = PersonProviderFailBack.class)
// @RibbonClient(value = "person-provider")
public interface PersonService {
    /**
     * savePerson
     * 
     * @param person
     * @return
     */
    @PostMapping("/person/save")
    boolean savePerson(@RequestBody Person person);

    /**
     * findAll
     * 
     * @return
     */
    @GetMapping("/person/findAll")
    Collection<Person> findAll();

}
