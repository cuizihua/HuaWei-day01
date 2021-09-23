package com.baizhi.um.test;

import org.apache.ibatis.cache.Cache;
import redis.clients.jedis.Jedis;

public class TestJedis {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("hadoop10",6379);
    }
}
