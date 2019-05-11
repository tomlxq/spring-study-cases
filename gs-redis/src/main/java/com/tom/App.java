package com.tom;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ///System.out.println( "Hello World!" );

        RedisLock redisLock = new RedisLock();
        String testLock = redisLock.getLock("testLock", 10000);
        if (null != testLock) {
            System.out.println("获取锁成功 [" + testLock + "]");
        }
        String testLock2 = redisLock.getLock("testLock", 10000);
        System.out.println(testLock2);
    }
}
