package com.foo.concurrent.executorservice;

/**
 *
 * @ClassName: TestThread2
 * @Description: 【推荐】使用CountDownLatch进行异步转同步操作，每个线程退出前必须调用countDown方法，线程执行代码注意catch异常，确保countDown方法被执行到，避免主线程无法执行至await方法，直到超时才返回结果。 说明：注意，子线程抛出异常堆栈，不能在主线程try-catch到。
 * @Author: tomluo
 * @Date: 2022/12/17 19:43
 **/
public class TestThread2 {}