package com.tom;

public class OrderServiceImpl implements IOrderService {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        System.out.println(request);
        DoOrderResponse response = new DoOrderResponse();
        response.setCode("0");
        response.setMemo("处理成功");
        return response;
    }
}
