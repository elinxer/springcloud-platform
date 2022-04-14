/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elinxer.springcloud.platform.mqtt.broker.consumer;

import com.elinxer.springcloud.platform.mqtt.broker.config.MqttxConfig;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;


/**
 * 集群消息内部订阅分发处理器, rocketmq实现
 */
@Slf4j
public class RocketmqInternalListener extends AbstractInnerChannel implements MessageListenerConcurrently {

    public RocketmqInternalListener(List<Watcher> watchers, Serializer serializer, MqttxConfig mqttxConfig) {
        super(watchers, serializer, mqttxConfig);
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        list.stream().filter(messageExt -> messageExt != null).forEach(message -> {
            byte[] bodyBytes = message.getBody();
            String channel = message.getTopic();
            log.info("channel={}", channel);
            try {
                dispatch(bodyBytes, channel);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("consumeMessage ex={}", e);
            }

        });
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
