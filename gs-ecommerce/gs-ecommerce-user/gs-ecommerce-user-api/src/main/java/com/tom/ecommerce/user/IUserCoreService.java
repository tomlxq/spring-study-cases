package com.tom.ecommerce.user;


import com.tom.ecommerce.user.dto.UserLoginRequest;
import com.tom.ecommerce.user.dto.UserLoginResponse;
import com.tom.ecommerce.user.dto.UserRegisterRequest;
import com.tom.ecommerce.user.dto.UserRegisterResponse;

public interface IUserCoreService {

    /**
     * 用户登录操作
     *
     * @param userLoginRequest
     * @return
     */
    UserLoginResponse login(UserLoginRequest userLoginRequest);


    /*
     * 注册
     */
    UserRegisterResponse register(UserRegisterRequest userRegisterRequest);
}