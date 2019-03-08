package com.tom.user;

import com.tom.user.dal.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.jta.JtaTransactionManager;

@Service(value = "userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    IUserDao userDao;
    @Autowired
    JtaTransactionManager springTransactionManager;

    @Override
    public DoRechargeResponse doRecharge(DoRechargeRequest request) {
        System.out.println("你曾经来过：" + request);
        userDao.recharge();
        DoRechargeResponse response = new DoRechargeResponse();
        response.setCode("0");
        response.setMemo("更新用户成功");
        return response;
    }
}
