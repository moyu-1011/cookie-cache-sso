package com.example.cache.utils;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate;


    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 储存key-value
     *
     * @param key
     * @param v
     */
    public void set(String key, Object v) {
        redisTemplate.opsForValue().set(key, v);
    }


    /**
     * 储存key-value并设置过期时间
     *
     * @param key
     * @param v
     * @param timeout
     * @param unit
     */
    public void set(String key, Object v, long timeout, TimeUnit unit) {
        set(key, v);
        resetExpires(key, timeout, unit);
    }


    /**
     * 获取value
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * key是否存在
     *
     * @param key
     * @return
     */
    public boolean keyExists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        return redisTemplate.delete(key);
    }


    /**
     * 重新设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public boolean resetExpires(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }


    /**
     * 获取key过期剩余时间
     *
     * @param key
     * @param unit
     * @return
     */
    public Long getExpires(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

}
