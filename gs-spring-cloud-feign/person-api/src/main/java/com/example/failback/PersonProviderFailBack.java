package com.example.failback;

import java.util.Collection;
import java.util.Collections;

import com.example.domain.Person;
import com.example.service.PersonService;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/7/3
 */
@Slf4j
public class PersonProviderFailBack implements PersonService {

    @Override
    public boolean savePerson(Person person) {
        log.info("failed: save person failed");
        return false;
    }

    @Override
    public Collection<Person> findAll() {
        log.info("failed: findAll empty");
        return Collections.EMPTY_LIST;
    }
}
