package com.tom.demo;

import com.tom.DoOrderRequest;
import com.tom.DoOrderResponse;
import com.tom.IOrderService;

public class App {
    public static void main(String[] args) {
        IOrderService service = null;
        DoOrderRequest request = new DoOrderRequest();
        request.setName("tom");
        DoOrderResponse response = service.doOrder(request);
        System.out.println(response);
    }
}
