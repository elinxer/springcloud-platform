package com.elinxer.springcloud.platform.mqtt.broker.service.impl;

import com.elinxer.springcloud.platform.mqtt.broker.entity.InternalMessage;
import com.elinxer.springcloud.platform.mqtt.broker.service.IInternalMessagePublishService;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import org.springframework.kafka.core.KafkaTemplate;


/**
 * 基于 kafka 实现
 *
 * @see DefaultInternalMessagePublishServiceImpl
 */
public class KafkaInternalMessagePublishServiceImpl implements IInternalMessagePublishService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final Serializer serializer;

    public KafkaInternalMessagePublishServiceImpl(KafkaTemplate<String, byte[]> kafkaTemplate,
                                                  Serializer serializer) {
        this.kafkaTemplate = kafkaTemplate;
        this.serializer = serializer;
    }

    @Override
    public <T> void publish(InternalMessage<T> internalMessage, String channel) {
        kafkaTemplate.send(channel, serializer.serialize(internalMessage));
    }
}
