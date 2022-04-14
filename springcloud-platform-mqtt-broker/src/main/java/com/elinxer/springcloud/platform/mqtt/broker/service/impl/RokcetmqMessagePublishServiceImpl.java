package com.elinxer.springcloud.platform.mqtt.broker.service.impl;

import com.elinxer.springcloud.platform.mqtt.broker.entity.InternalMessage;
import com.elinxer.springcloud.platform.mqtt.broker.service.IInternalMessagePublishService;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;


/**
 * 基于 rokcetmq 实现
 *
 * @see RokcetmqMessagePublishServiceImpl
 */
@Slf4j
public class RokcetmqMessagePublishServiceImpl implements IInternalMessagePublishService {

    private final RocketMQTemplate rocketMQTemplate;

    private final Serializer serializer;

    public RokcetmqMessagePublishServiceImpl(RocketMQTemplate rocketMQTemplate,
                                             Serializer serializer) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.serializer = serializer;
    }

    @Override
    public <T> void publish(InternalMessage<T> internalMessage, String channel) {
        try {
            Message message = new Message();
            message.setBody(serializer.serialize(internalMessage));
            message.setTopic(channel);
            rocketMQTemplate.getProducer().send(message);
        } catch (Exception e) {
            log.error("publish ex={}", e);
        }
    }


}
