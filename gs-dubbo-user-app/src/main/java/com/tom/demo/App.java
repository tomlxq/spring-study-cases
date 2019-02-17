package com.tom.demo;

import com.alibaba.dubbo.rpc.RpcContext;
import com.tom.DoOrderRequest;
import com.tom.DoOrderResponse;
import com.tom.IOrderQueryService;
import com.tom.IOrderService;
import com.tom.IUserService;
import com.tom.UserLoginRequest;
import com.tom.UserLoginResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class App {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/user-consumer.xml");
        IOrderService service = (IOrderService) context.getBean("orderServices");
        DoOrderRequest request = new DoOrderRequest();
        request.setName("tom");
        DoOrderResponse response = service.doOrder(request);
        System.out.println(response);

        IOrderQueryService orderQueryService = (IOrderQueryService) context.getBean("orderQueryServices");
        String message = orderQueryService.doQueryOrder("tom");
        System.out.println(message);
//多版本支持
        IOrderService serviceV2 = (IOrderService) context.getBean("orderServicesV2");
        request = new DoOrderRequest();
        request.setName("jack");
        response = serviceV2.doOrder(request);
        System.out.println(response);
//异步调用
        IOrderQueryService orderQueryServiceAsync = (IOrderQueryService) context.getBean("orderQueryServiceAsync");
        orderQueryServiceAsync.doQueryOrder("Green异步调用");
        Future<String> future = RpcContext.getContext().getFuture();
        System.out.println(future.get());

        IUserService userService = (IUserService) context.getBean("userServices");
        UserLoginRequest request1 = new UserLoginRequest();
        request1.setName("jack_login");
        request1.setPassword("123456");
        UserLoginResponse res = userService.login(request1);
        System.out.println(res);
        System.in.read();


    }
}
