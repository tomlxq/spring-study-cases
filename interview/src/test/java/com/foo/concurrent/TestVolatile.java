package com.foo.concurrent;

/**
 *
 * @ClassName: TestVolatile
 * @Description: 【参考】volatile解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但是如果多写，同样无法解决线程安全问题。
 * 如果是count++操作，使用如下类实现：
 *  AtomicInteger count = new AtomicInteger();
 *  count.addAndGet(1);
 * 如果是JDK8，推荐使用LongAdder对象，比AtomicLong性能更好（减少乐观锁的重试次数）。
 * @Author: tomluo
 * @Date: 2022/12/17 19:49
 **/
public class TestVolatile {}