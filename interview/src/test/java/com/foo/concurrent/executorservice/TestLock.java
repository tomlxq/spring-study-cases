package com.foo.concurrent.executorservice;

/**
 *
 * @ClassName: TestLock
 * @Description:
 * 【强制】高并发时，同步调用应该去考量锁的性能损耗。
 * 能用无锁数据结构，就不要用锁；
 * 能锁区块，就不要锁整个方法体；
 * 能用对象锁，就不要用类锁
 * 【强制】对多个资源、数据库表、对象同时加锁时，需要保持一致的加锁顺序，否则可能会造成死锁。
 * 【强制】并发修改同一记录时，避免更新丢失，需要加锁。要么在应用层加锁，要么在缓存加锁，要么在数据库层使用乐观锁，使用version作为更新依据。
 * 说明：如果每次访问冲突概率小于20%，推荐使用乐观锁，否则使用悲观锁。乐观锁的重试次数不得小于3次。
 * @Author: tomluo
 * @Date: 2022/12/17 19:30
 **/
public class TestLock {}