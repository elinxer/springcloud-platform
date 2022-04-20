package com.elinxer.springcloud.platform.cache.serializer.jdk;

import com.elinxer.springcloud.platform.cache.serializer.Serializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Serializer for serialize and deserialize.
 *
 * @author elinx
 * @version 3.0.0
 */
public class JdkSerializer implements Serializer {

    private JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();


    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        return jdkSerializationRedisSerializer.serialize(obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data) throws SerializationException {
        return (T) jdkSerializationRedisSerializer.deserialize(data);
    }

}
