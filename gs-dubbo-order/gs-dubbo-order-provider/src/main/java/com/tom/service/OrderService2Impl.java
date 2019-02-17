package com.tom.service;

import com.tom.DoOrderRequest;
import com.tom.DoOrderResponse;
import com.tom.IOrderService;
import org.springframework.stereotype.Service;

@Service(value = "orderServiceV2")
public class OrderService2Impl implements IOrderService {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        System.out.println("你曾经来过version2：" + request);
        DoOrderResponse response = new DoOrderResponse();
        response.setCode("0");
        response.setMemo("处理成功version2");
        return response;
    }
}
