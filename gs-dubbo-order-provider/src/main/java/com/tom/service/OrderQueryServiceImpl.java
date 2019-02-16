package com.tom.service;

import com.tom.IOrderQueryService;
import org.springframework.stereotype.Service;

@Service(value = "orderQueryService")
public class OrderQueryServiceImpl implements IOrderQueryService {

    @Override
    public String doQueryOrder(String para) {
        System.out.println("查询参数" + para);
        return "hello," + para;
    }
}
