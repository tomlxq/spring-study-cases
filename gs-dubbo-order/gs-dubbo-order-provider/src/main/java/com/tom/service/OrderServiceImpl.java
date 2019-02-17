package com.tom.service;

import com.tom.DoOrderRequest;
import com.tom.DoOrderResponse;
import com.tom.IOrderService;

public class OrderServiceImpl implements IOrderService {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        System.out.println("你曾经来过：" + request);
        DoOrderResponse response = new DoOrderResponse();
        response.setCode("0");
        response.setMemo("处理成功");
        return response;
    }
}
