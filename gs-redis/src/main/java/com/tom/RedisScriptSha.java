package com.tom;

import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;

public class RedisScriptSha {

    public static final String key = "ip:limits:127.0.0.1";

    public static void main(String[] args) {
        try {
            Jedis jedisPool = JedisManager.getJedisPool();
            String lua = "--限流，对某个IP限流，超过3次，禁止访问\n" +
                    "local num=redis.call('incr',KEYS[1])\n" +
                    "if tonumber(num)==1 then\n" +
                    "        redis.call('expire',KEYS[1],ARGV[1])\n" +
                    "        return 1\n" +
                    "elseif  tonumber(num)>tonumber(ARGV[2]) then\n" +
                    "        return 0\n" +
                    "else \n" +
                    "        return 1\n" +
                    "end";
            List<String> keys = Arrays.asList(new String[]{key});
            List<String> values = Arrays.asList(new String[]{"6000", "3"});
            //运行摘要
            Object ret = jedisPool.evalsha(jedisPool.scriptLoad(lua), keys, values);
            System.out.println(ret);
            System.out.println(jedisPool.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
