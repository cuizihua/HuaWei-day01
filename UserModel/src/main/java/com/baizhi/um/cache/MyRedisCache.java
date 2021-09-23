package com.baizhi.um.cache;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.ibatis.cache.Cache;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 把需要缓存的数据存入到redis里面。在从缓存中取数据的时候，就到redis中取
 *
 * 选择hash类型作为数据结构
 * hash的大key就是代码中的id
 * hash的小key就是代码中的key
 * hash的value就是代码中的value
 */
public class MyRedisCache  implements Cache {

    private Jedis jedis;

    private final String id;//映射文件中的namespace

    public MyRedisCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        //key/value可以转换成字节数组==》k,v
        byte[] k = SerializationUtils.serialize((Serializable) key);
        byte[] v = SerializationUtils.serialize((Serializable) value);
        byte[] i = SerializationUtils.serialize((Serializable) id);

        jedis.hset(i,k,v);

    }

    @Override
    public Object getObject(Object key) {
        byte[] k = SerializationUtils.serialize((Serializable) key);
        byte[] i = SerializationUtils.serialize((Serializable) id);
        byte[] v = jedis.hget(i,k);
        Object value = SerializationUtils.deserialize(v);
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return null;
    }


    //执行增删改的时候，把缓存中的数据清空：防止脏读
    @Override
    public void clear() {

//        jedis.flushDB();//把这个数据库中的数据全部删除啦
        byte[] i = SerializationUtils.serialize((Serializable) id);
        jedis.del(i);//把对应的缓存数据清空
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
