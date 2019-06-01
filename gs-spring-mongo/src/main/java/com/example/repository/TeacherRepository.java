package com.example.repository;

import com.example.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TeacherRepository extends MongoRepository<Teacher, Long> {
    Teacher findByPersonId(Long personId);

    //分页查询满足条件：name = name and age >= age limit pageable
    @Query(value = "{\"name\":{\"$regex\":?0},\"age\":{\"$gte\":?1}}")
    Page<Teacher> findByNameAndAge2(String name, int age, Pageable pageable);

    int deleteByPersonId(Long personId);

    //删除满足条件：age <= age1 and age >= age2
    @Query("{\"age\":{\"$lte\":?0},\"$gte\":?1}")
    int deleteByAge2(int age1,int age2);

    //分页查询满足条件：name = name and age >= age limit pageable,只会查询相互personId和age的值
    @Query(value = "{\"name\":{\"$regex\":?0},\"age\":{\"$gte\":?1}}",fields = "{\"personId\":0,\"address\":0}")
    Page<Teacher> findByAge2(String name, int age, Pageable pageable);


}
