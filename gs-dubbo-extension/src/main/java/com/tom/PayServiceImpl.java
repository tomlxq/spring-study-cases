package com.tom;

public class PayServiceImpl implements IPayService {
    @Override
    public void payMoney() {
        System.out.println("我付款了！");
    }
}
