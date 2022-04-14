/**
 * @description
 * @author elinx
 * @create 2021-10-11 19:55
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants.TopicConstants;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgBodyType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import com.elinxer.springcloud.platform.mqtt.broker.service.ICommandService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

@TboxPayload(type = MsgBodyType.ALARM_INFO)
public class AlarmInfoHandler extends AbstractPayloadHandler{

    public AlarmInfoHandler(RocketMQTemplate rocketMQTemplate, ICommandService commandService){
        super(rocketMQTemplate,commandService);
    }

    @Override
    public String getTopic() {
        return TopicConstants.TBOX_ALARM_INFO_TOPIC;
    }

    @Override
    public void handler(MessageProto msg, byte[] payload, ChannelHandlerContext ctx) {
        sendMsgToMq(msg);
    }
}