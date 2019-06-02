package com.example.service;

import com.example.dao.UserMapper;
import com.example.domain.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //注意：方法的@Transactional会覆盖类上面声明的事务
    //Propagation.REQUIRED ：有事务就处于当前事务中，没事务就创建一个事务
    //isolation=Isolation.DEFAULT：事务数据库的默认隔离级别
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
    public void insertUser(User u) {
        this.userMapper.insert(u);

        //如果类上面没有@Transactional,方法上也没有，哪怕throw new RuntimeException,数据库也会成功插入数据
        //	throw new RuntimeException("测试插入事务");
    }

    public PageInfo<User> queryPage(String userName, int pageNum, int pageSize) {
        Page<User> page = PageHelper.startPage(pageNum, pageSize);
        //PageHelper会自动拦截到下面这查询sql
        this.userMapper.query(userName);
        return page.toPageInfo();
    }


    //测试事务
    //注意：方法的@Transactional会覆盖类上面声明的事务
    //Propagation.REQUIRED ：有事务就处于当前事务中，没事务就创建一个事务
    //isolation=Isolation.DEFAULT：事务数据库的默认隔离级别
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
    public void testTransational() {

        //删除全部
        this.userMapper.deleteAll();
        //新增
        User u = new User();
        u.setId(123456);
        u.setUsername("张三");
        this.userMapper.insert(u);
        //制造异常
        //如果类上面没有@Transactional,方法上也没有，哪怕throw new RuntimeException,数据库也会提交
        throw new RuntimeException("事务异常测试");
    }

}