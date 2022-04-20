package com.elinxer.springcloud.platform.cache.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 此时定义的序列化操作表示可以序列化所有类的对象，当然，这个对象所在的类一定要实现序列化接口
 */
public class RedisObjectSerializer implements RedisSerializer<Object> {

    // 做一个空数组，不是null
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        // 这个时候没有要序列化的对象出现，所以返回的字节数组应该就是一个空数组
        if (obj == null) {
            return EMPTY_BYTE_ARRAY;
        }
        // 将对象变为字节数组
        return SerializerManager.getSerializer(SerializerManager.HESSIAN2).serialize(obj);
    }

    @Override
    public Object deserialize(byte[] data) throws SerializationException {
        // 此时没有对象的内容信息
        if (data == null || data.length == 0) {
            return null;
        }
        return SerializerManager.getSerializer(SerializerManager.HESSIAN2).deserialize(data);
    }

}