package com.elinxer.springcloud.platform.mqtt.broker.consumer;


import com.elinxer.springcloud.platform.mqtt.broker.config.MqttxConfig;
import com.elinxer.springcloud.platform.mqtt.broker.entity.InternalMessage;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 该抽象类有两个子类 {@link KafkaInternalMessageSubscriber} 和 {@link  DefaultInternalMessageSubscriber},
 * 具体采用哪个实现取决于用户配置 <code>mqttx.enable-cluster.type</code>
 */
public abstract class AbstractInnerChannel {

    private final int brokerId;
    private final Serializer serializer;
    private final List<Watcher> watchers;

    public AbstractInnerChannel(List<Watcher> watchers, Serializer serializer, MqttxConfig mqttxConfig) {
        Assert.notNull(watchers, "watchers can't be null");
        Assert.notNull(mqttxConfig, "mqttxConfig can't be null");
        Assert.notNull(serializer, "serializer can't be null");

        this.watchers = watchers;
        this.brokerId = mqttxConfig.getBrokerId();
        this.serializer = serializer;
    }


    /**
     * 分发集群消息，当前处理类别：
     * <ol>
     *     <li>客户端连接断开 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#DISCONNECT}</li>
     *     <li>发布消息_qos012 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#PUB}</li>
     *     <li>发布消息响应_qos1 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#PUB_ACK}</li>
     *     <li>发布消息接收响应_qos2 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#PUB_REC}</li>
     *     <li>发布消息释放_qos2 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#PUB_REL}</li>
     *     <li>发布消息完成_qos2 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#PUB_COM}</li>
     *     <li>用户权限修改 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#ALTER_USER_AUTHORIZED_TOPICS}</li>
     *     <li>订阅与删除订阅 {@link com.elinxer.springcloud.platform.mqtt.broker.constants.InternalMessageEnum#SUB_UNSUB}</li>
     * </ol>
     *
     * @param message 消息内容
     * @param channel 订阅频道
     */
    @SuppressWarnings("rawtypes")
    public void dispatch(byte[] message, String channel) {
        // 同 broker 消息屏蔽
        InternalMessage internalMessage = serializer.deserialize(message, InternalMessage.class);
        if (brokerId == internalMessage.getBrokerId()) {
            return;
        }

        for (Watcher watcher : watchers) {
            if (watcher.support(channel)) {
                watcher.action(message);
            }
        }
    }
}
