package com.example.controller;

import com.tom.DoOrderRequest;
import com.tom.DoOrderResponse;
import com.tom.IOrderService;
import com.tom.IUserService;
import com.tom.UserLoginRequest;
import com.tom.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/index/")
public class HomeController {
    @Autowired
    @Qualifier(value = "orderServices")
    IOrderService orderService;
    @Autowired
    IUserService userService;

    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public DoOrderResponse index(HttpServletRequest req) {
        DoOrderRequest request = new DoOrderRequest();
        request.setName("tom_app_demo");
        DoOrderResponse response = orderService.doOrder(request);
        System.out.println(response);
        UserLoginRequest request1 = new UserLoginRequest();
        request1.setName("jack_login");
        request1.setPassword("123456");
        UserLoginResponse res = userService.login(request1);
        System.out.println(res);
        return response;
    }
}
