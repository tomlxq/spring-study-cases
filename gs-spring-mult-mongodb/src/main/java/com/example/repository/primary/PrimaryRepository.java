package com.example.repository.primary;


import com.example.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PrimaryRepository extends MongoRepository<User, String> {
}