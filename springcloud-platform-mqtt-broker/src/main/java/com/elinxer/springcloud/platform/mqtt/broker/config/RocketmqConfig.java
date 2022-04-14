/**
 * @description
 * @author caoxiaoguang
 * @create 2021-09-06 15:31
 **/
package com.elinxer.springcloud.platform.mqtt.broker.config;

import com.elinxer.springcloud.platform.mqtt.broker.constants.ClusterTopic;
import com.elinxer.springcloud.platform.mqtt.broker.consumer.RocketmqInternalListener;
import com.elinxer.springcloud.platform.mqtt.broker.consumer.Watcher;
import com.elinxer.springcloud.platform.mqtt.broker.service.IInternalMessagePublishService;
import com.elinxer.springcloud.platform.mqtt.broker.service.impl.RokcetmqMessagePublishServiceImpl;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Slf4j
@Configuration
public class RocketmqConfig {

    public static final String INNER_CONSUMER_GROUP = "INNER_CONSUMER_GROUP";

    @Value("${rocketmq.name-server}")
    private String nameServer;

    private static final String ROCKETMQ = "rocketmq";

    @Bean
    RocketmqInternalListener rocketmqInternalListener(List<Watcher> watchers, Serializer serializer, MqttxConfig mqttxConfig) {
        return new RocketmqInternalListener(watchers, serializer, mqttxConfig);
    }

    /**
     * mqtt内部集群消费
     *
     * @param messageListener
     * @return
     */
    @Bean(name = "innerConsumer")
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = ROCKETMQ, matchIfMissing = true)
    DefaultMQPushConsumer innerConsumer(RocketmqInternalListener messageListener) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(INNER_CONSUMER_GROUP);
        consumer.setNamesrvAddr(nameServer);
        //设置最大重试次数  默认-1 无限重试
        consumer.setMaxReconsumeTimes(3);
        consumer.setMessageModel(MessageModel.BROADCASTING);
        // 设置消费地点,从最后一个进行消费(其实就是消费策略)
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        try {
            consumer.subscribe(ClusterTopic.PUB, "*");
            consumer.subscribe(ClusterTopic.PUB_ACK, "*");
            consumer.subscribe(ClusterTopic.PUB_COM, "*");
            consumer.subscribe(ClusterTopic.PUB_REC, "*");
            consumer.subscribe(ClusterTopic.PUB_REL, "*");
            consumer.subscribe(ClusterTopic.SUB_UNSUB, "*");
            consumer.subscribe(ClusterTopic.CONNECT, "*");
            consumer.subscribe(ClusterTopic.DISCONNECT, "*");
            consumer.subscribe(ClusterTopic.ALTER_USER_AUTHORIZED_TOPICS, "*");
            // 注册监听器
            consumer.registerMessageListener(messageListener);
            consumer.start();
        } catch (Exception e) {
            log.info("innerConsumer ex={}", e);
        }
        return consumer;
    }

    /**
     * mqtt集群内部消息转发
     *
     * @param rocketMQTemplate
     * @param serializer
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "com.hx.broker.cluster.type", havingValue = ROCKETMQ)
    IInternalMessagePublishService rokcetmqMessagePublishServiceImpl(RocketMQTemplate rocketMQTemplate, Serializer serializer) {
        return new RokcetmqMessagePublishServiceImpl(rocketMQTemplate, serializer);
    }


}