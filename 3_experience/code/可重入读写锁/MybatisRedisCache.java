package com.sft.ccs.support.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.HashOperations;

/**
 * mybatis redis 二级缓存
 */
public class MybatisRedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;

    private static HashOperations<Object, Object, Object> hashOps;

    public static void setHashOps(HashOperations<Object, Object, Object> hashOps) {
        MybatisRedisCache.hashOps = hashOps;
    }

    public MybatisRedisCache(String id) {
        this.id = "dao:" + StringUtils.substringAfterLast(id, ".");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        hashOps.put(id, key.toString(), value);
    }

    @Override
    public Object getObject(Object key) {
        return hashOps.get(id, key.toString());
    }

    @Override
    public Object removeObject(Object key) {
        return hashOps.delete(id, key.toString());
    }

    @Override
    public void clear() {
        hashOps.getOperations().delete(id);
    }

    @Override
    public int getSize() {
        return hashOps.size(id).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
