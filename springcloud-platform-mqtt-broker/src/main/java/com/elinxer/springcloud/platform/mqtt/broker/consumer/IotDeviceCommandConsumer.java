package com.elinxer.springcloud.platform.mqtt.broker.consumer;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants.TopicConstants;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.entity.InMqMessageEntity;
import com.elinxer.springcloud.platform.mqtt.broker.service.ICommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * Iot指令下发消费
 */
@Component
@RocketMQMessageListener(topic = TopicConstants.TBOX_VECHICLE_CONTROL_TOPIC, consumerGroup = TopicConstants.TBOX_VECHICLE_CONTROL_GROUP, messageModel = MessageModel.BROADCASTING)
@Slf4j
public class IotDeviceCommandConsumer implements RocketMQListener<InMqMessageEntity> {

    private ICommandService commandService;

    public IotDeviceCommandConsumer(ICommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public void onMessage(InMqMessageEntity entity) {
        try {
            commandService.sendToDevice(entity);
        } catch (Exception e) {
            log.error("ex={}", e);
        }

    }
}