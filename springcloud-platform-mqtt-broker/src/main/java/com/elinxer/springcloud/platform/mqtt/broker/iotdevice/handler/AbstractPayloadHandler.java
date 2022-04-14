package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler;
/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-08 17:54
 **/


import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.entity.OutMqMessageEntity;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgHeader;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import com.elinxer.springcloud.platform.mqtt.broker.service.ICommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

@Slf4j
public abstract class AbstractPayloadHandler implements PayloadHandler {

    protected RocketMQTemplate rocketMQTemplate;

    protected ICommandService commandService;

    public AbstractPayloadHandler(RocketMQTemplate rocketMQTemplate,ICommandService commandService){
        this.rocketMQTemplate=rocketMQTemplate;
        this.commandService=commandService;
    }

    public abstract String getTopic();

    public void sendMsgToMq(MessageProto messageProto){
        OutMqMessageEntity entity =OutMqMessageEntity.of(messageProto);
        rocketMQTemplate.asyncSendOrderly(getTopic(), entity, messageProto.getHeader().getDeviceID(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                  log.info("deviceId={} success",messageProto.getHeader().getDeviceID());
            }
            @Override
            public void onException(Throwable throwable) {
                log.info("deviceId={}  failed ex={}",messageProto.getHeader().getDeviceID(),throwable);
            }
        });

    }

    public void sendMsgToMq(MsgHeader header,byte[]payload){

        OutMqMessageEntity entity =OutMqMessageEntity.of(payload,header);
        rocketMQTemplate.asyncSendOrderly(getTopic(), entity, header.getDeviceID(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("deviceId={} success",header.getDeviceID());
            }
            @Override
            public void onException(Throwable throwable) {
                log.info("deviceId={}  failed ex={}",header.getDeviceID(),throwable);
            }
        });

    }


}
