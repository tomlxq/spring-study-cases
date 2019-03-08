package com.example.demo.controller;

import com.tom.order.DoOrderRequest;
import com.tom.order.DoOrderResponse;
import com.tom.order.IOrderService;
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

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public DoOrderResponse index(HttpServletRequest req) {
        DoOrderRequest request = new DoOrderRequest();
        request.setName("tom_app_demo");
        DoOrderResponse response = orderService.doOrder(request);
        System.out.println(response);

        return response;
    }
}
