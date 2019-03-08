package com.tom.order;

import com.tom.order.dal.OrderDao;
import com.tom.user.DoRechargeRequest;
import com.tom.user.DoRechargeResponse;
import com.tom.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;

@Service(value = "orderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    IUserService userService;
    @Autowired
    JtaTransactionManager springTransactionManager;

    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        System.out.println("你曾经来过：" + request);
        TransactionManager transactionManager = springTransactionManager.getTransactionManager();
        try {
            transactionManager.begin();
            //order下单之后更新账户余额
            orderDao.insertOrder();
            DoRechargeRequest doRechargeRequest = new DoRechargeRequest();
            doRechargeRequest.setName("jack");
            DoRechargeResponse doRechargeResponse = userService.doRecharge(doRechargeRequest);
            System.out.println(doRechargeResponse);
            transactionManager.commit();
        } catch (Exception e) {
            try {
                transactionManager.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        DoOrderResponse response = new DoOrderResponse();
        response.setCode("0");
        response.setMemo("处理成功");
        return response;
    }
}
