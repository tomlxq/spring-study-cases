package com.foo.concurrent.executorservice;

/**
 *
 * @ClassName: TestThreadLocal
 * @Description: 【参考】ThreadLocal无法解决共享对象的更新问题，ThreadLocal对象建议使用static修饰。这个变量是针对一个线程内所有操作共享的，所以设置为静态变量，所有此类实例共享此静态变量 ，也就是说在类第一次被使用时装载，只分配一块存储空间，所有此类的对象(只要是这个线程内定义的)都可以操控这个变量。
 * @Author: tomluo
 * @Date: 2022/12/17 19:52
 **/
public class TestThreadLocal {}