package com.foo.concurrent.executorservice;

/**
 *
 * @ClassName: TestLock2
 * @Description:
 * 【强制】并发修改同一记录时，避免更新丢失，需要加锁。要么在应用层加锁，要么在缓存加锁，要么在数据库层使用乐观锁，使用version作为更新依据。
 * 说明：如果每次访问冲突概率小于20%，推荐使用乐观锁，否则使用悲观锁。乐观锁的重试次数不得小于3次。
 * @Author: tomluo
 * @Date: 2022/12/17 19:40
 **/
public class TestLock2 {}