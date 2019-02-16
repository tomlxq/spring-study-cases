package com.tom.service;

import com.tom.IUserService;
import com.tom.UserLoginRequest;
import com.tom.UserLoginResponse;
import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        System.out.println("登陆:" + request);
        UserLoginResponse response = new UserLoginResponse();
        response.setCode("0");
        response.setName(request.getName());
        System.out.println(JSON.toString(response));
        return response;
    }
}
