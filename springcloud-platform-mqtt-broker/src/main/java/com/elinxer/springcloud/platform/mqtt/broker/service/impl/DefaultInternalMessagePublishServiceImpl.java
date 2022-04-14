package com.elinxer.springcloud.platform.mqtt.broker.service.impl;

import com.elinxer.springcloud.platform.mqtt.broker.entity.InternalMessage;
import com.elinxer.springcloud.platform.mqtt.broker.service.IInternalMessagePublishService;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于 Redis 的实现
 */
@Slf4j
public class DefaultInternalMessagePublishServiceImpl implements IInternalMessagePublishService {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final Serializer serializer;

    public DefaultInternalMessagePublishServiceImpl(RedisTemplate<String, byte[]> redisTemplate, Serializer serializer) {
        this.redisTemplate = redisTemplate;
        this.serializer = serializer;
    }

    @Override
    public <T> void publish(InternalMessage<T> internalMessage, String channel) {
        redisTemplate.convertAndSend(channel, serializer.serialize(internalMessage));
    }
}