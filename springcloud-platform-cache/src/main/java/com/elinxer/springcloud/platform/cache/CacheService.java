package com.elinxer.springcloud.platform.cache;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author elinx
 */
interface CacheService {

    /**
     * 设置缓存
     *
     * @param key      缓存 KEY
     * @param value    缓存值
     * @param timeout  缓存过期时间
     * @param timeUnit 缓存过期时间单位
     */
    void setCache(String key, String value, long timeout, TimeUnit timeUnit);

    /**
     * 设置缓存
     *
     * @param key     缓存 KEY
     * @param value   缓存值
     * @param timeout 缓存过期时间
     */
    void setCache(String key, String value, long timeout);

    /**
     * 设置缓存 不过期
     *
     * @param key   缓存KEY
     * @param value 缓存值
     */
    void setCache(String key, String value);

    /**
     * 设置对象缓存
     *
     * @param key   缓存KEY
     * @param value 缓存值
     */
    void setObjectCache(String key, Object value);

    /**
     * 设置对象缓存
     *
     * @param key     缓存KEY
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void setObjectCache(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 删除一个指定的Object
     *
     * @param key 键名称
     */
    void deleteObject(String key);

    /**
     * 获取缓存
     *
     * @param key 缓存KEY
     * @return String
     */
    String getCache(String key);

    /**
     * 获取对象缓存内容
     *
     * @param key 缓存KEY
     * @return Object
     */
    Object getObjectCache(String key);

    /**
     * 获取key的存活时间
     *
     * @param key 缓存KEY
     * @return long
     */
    long getExpireCache(String key);

    /**
     * 设置超时时间
     *
     * @param key      缓存KEY
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return Boolean
     */
    Boolean expireCache(String key, long timeout, TimeUnit timeUnit);

    /**
     * 批量获取缓存值
     *
     * @param key 缓存KEY
     * @return Set<String>
     */
    Set<String> keysCache(String key);

    /**
     * 获取缓存
     *
     * @param key     缓存KEY
     * @param closure Closure<V, K>
     * @return <V, K> String
     */
    <V, K> String getCache(K key, com.elinxer.springcloud.platform.cache.Closure<V, K> closure);

    /**
     * 获取缓存
     *
     * @param key      缓存KEY
     * @param closure  Closure<V, K>
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return <V, K> String
     */
    <V, K> String getCache(K key, com.elinxer.springcloud.platform.cache.Closure<V, K> closure, long timeout, TimeUnit timeUnit);

    /**
     * 删除缓存
     *
     * @param key 缓存KEY
     */
    void deleteCache(String key);

    /**
     * 批量删除缓存
     *
     * @param key 缓存KEY
     */
    void deleteBatchCache(String key);

    /**
     * 获取缓存基础句柄
     *
     * @return StringRedisTemplate
     */
    StringRedisTemplate getStrHandle();

}
