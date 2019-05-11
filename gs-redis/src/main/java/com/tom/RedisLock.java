package com.tom;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

public class RedisLock {
    public String getLock(String key, int timeout) {
        try {
            Jedis jedisPool = JedisManager.getJedisPool();
            String value = UUID.randomUUID().toString();
            long end = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < end) {//阻塞
                if (1 == jedisPool.setnx(key, value)) {
                    jedisPool.expire(key, timeout);
                    //锁设置成功，redis操作成功
                    return value;
                }
                if (jedisPool.ttl(key) == -1) {//检测过期时间
                    jedisPool.expire(key, timeout);
                }
                Thread.sleep(1000);//为了提高效率1秒后再次执行

            }
            return null;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public boolean releaseLock(String key, String value) {
        try {
            Jedis jedisPool = JedisManager.getJedisPool();
            jedisPool.watch(key);//防止其它线程正在修改
            while (true) {
                if (jedisPool.get(key).equals(value)) {//判断获得锁的线程和当前redis存在的锁是不是同一个
                    Transaction multi = jedisPool.multi();
                    multi.del(key);
                    List<Object> exec = multi.exec();
                    if (null == exec) {
                        continue;
                    }
                    break;
                }
            }
            jedisPool.unwatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
