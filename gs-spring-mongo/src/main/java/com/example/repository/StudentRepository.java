package com.example.repository;

import com.example.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface StudentRepository extends MongoRepository<Student, Long> {

    Student findByPersonId(Long personId);

    //分页查询满足条件：name = name and age >= age limit pageable
    @Query(value = "{\"name\":{\"$regex\":?0},\"age\":{\"$gte\":?1}}")
    Page<Student> findByNameAndAge2(String name, int age, Pageable pageable);

    int deleteByPersonId(Long personId);

    //删除满足条件：age >= age1 and age <= age2
    @Query("{\"age\":{\"$gte\":?0},\"$lte\":?1}")
    int deleteByAge2(int age1,int age2);

    //分页查询满足条件：name = name and age >= age limit pageable,只会查询相互personId和age的值
    @Query(value = "{\"name\":{\"$regex\":?0},\"age\":{\"$gte\":?1}}",fields = "{\"personId\":1,\"age\":1}")
    Page<Student> findByAge2(String name, int age, Pageable pageable);


}
