package com.example.service;

import com.example.domain.User;

public interface UserService {
    public User getUserById(int userId);

    boolean addUser(User record);

}