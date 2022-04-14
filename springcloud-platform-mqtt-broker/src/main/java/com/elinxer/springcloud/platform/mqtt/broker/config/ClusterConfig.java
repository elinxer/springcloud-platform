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

package com.elinxer.springcloud.platform.mqtt.broker.config;

import com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum;
import com.elinxer.springcloud.platform.mqtt.broker.consumer.DefaultInternalMessageSubscriber;
import com.elinxer.springcloud.platform.mqtt.broker.consumer.KafkaInternalMessageSubscriber;
import com.elinxer.springcloud.platform.mqtt.broker.consumer.Watcher;
import com.elinxer.springcloud.platform.mqtt.broker.service.IInternalMessagePublishService;
import com.elinxer.springcloud.platform.mqtt.broker.service.impl.DefaultInternalMessagePublishServiceImpl;
import com.elinxer.springcloud.platform.mqtt.broker.service.impl.KafkaInternalMessagePublishServiceImpl;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;
import java.util.concurrent.Executors;


/**
 * 集群配置, 两种实现:
 * 默认采用 redis 实现
 */
@Configuration
@ConditionalOnExpression("${com.hx.broker.cluster.enable:true} && !${com.hx.broker.enable-test-mode:false}")
public class ClusterConfig {

    public static final String REDIS = "redis";
    public static final String KAFKA = "kafka";
    private static final String ROCKETMQ = "rocketmq";

    @Bean
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = REDIS, matchIfMissing = true)
    public DefaultInternalMessageSubscriber defaultInternalMessageSubscriber(List<Watcher> watchers,
                                                                             Serializer serializer,
                                                                             MqttxConfig mqttxConfig) {
        return new DefaultInternalMessageSubscriber(watchers, serializer, mqttxConfig);
    }

    @Bean
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = REDIS, matchIfMissing = true)
    public IInternalMessagePublishService defaultInternalMessagePublishServiceImpl(RedisTemplate<String, byte[]> redisTemplate,
                                                                                   Serializer serializer) {
        return new DefaultInternalMessagePublishServiceImpl(redisTemplate, serializer);
    }

    @Bean
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = KAFKA)
    public KafkaInternalMessageSubscriber kafkaInternalMessageSubscriber(List<Watcher> watchers,
                                                                         Serializer serializer, MqttxConfig mqttxConfig) {
        return new KafkaInternalMessageSubscriber(watchers, serializer, mqttxConfig);
    }

    @Bean
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = KAFKA)
    public IInternalMessagePublishService kafkaInternalMessagePublishServiceImpl(KafkaTemplate<String, byte[]> kafkaTemplate, Serializer serializer) {
        return new KafkaInternalMessagePublishServiceImpl(kafkaTemplate, serializer);
    }

    /**
     * 消息适配器
     *
     * @param subscriber 消息订阅者
     */
    @Bean
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = REDIS, matchIfMissing = true)
    public MessageListener messageListenerAdapter(DefaultInternalMessageSubscriber subscriber) {
        MessageListenerAdapter mla = new MessageListenerAdapter();
        mla.setDelegate(subscriber);
        mla.setSerializer(RedisSerializer.byteArray());
        return mla;
    }


    /**
     * 消息监听者容器
     *
     * @param redisConnectionFactory {@link RedisConnectionFactory}
     * @param messageListener        {@link MessageListener}
     */
    @Bean
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = REDIS, matchIfMissing = true)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                       MessageListener messageListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        // 替换默认实现（默认实现未使用线程池）, 使用单线程执行任务
        redisMessageListenerContainer.setTaskExecutor(Executors.newSingleThreadExecutor());

        Map<MessageListener, Collection<? extends Topic>> listenerMap = new HashMap<>();
        List<Topic> list = new ArrayList<>(8);
        list.add(new ChannelTopic(InternalMessageEnum.PUB.getChannel()));
        list.add(new ChannelTopic(InternalMessageEnum.PUB_ACK.getChannel()));
        list.add(new ChannelTopic(InternalMessageEnum.PUB_REC.getChannel()));
        list.add(new ChannelTopic(InternalMessageEnum.PUB_COM.getChannel()));
        list.add(new ChannelTopic(InternalMessageEnum.PUB_REL.getChannel()));
        list.add(new ChannelTopic(InternalMessageEnum.DISCONNECT.getChannel()));
        list.add(new ChannelTopic(InternalMessageEnum.ALTER_USER_AUTHORIZED_TOPICS.getChannel()));
        list.add(new ChannelTopic(InternalMessageEnum.SUB_UNSUB.getChannel()));
        listenerMap.put(messageListener, list);

        redisMessageListenerContainer.setMessageListeners(listenerMap);

        return redisMessageListenerContainer;
    }

  /*  @Bean
    public MessageDelegatingHandler messageDelegatingHandler(List<MqttMessageHandler> list){
        return new MessageDelegatingHandler(list);
    }*/


}
