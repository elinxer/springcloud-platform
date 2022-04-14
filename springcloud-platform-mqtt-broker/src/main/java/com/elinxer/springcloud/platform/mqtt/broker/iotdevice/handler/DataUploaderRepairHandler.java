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

@TboxPayload(type = MsgBodyType.MSG_REISSUING)
public class DataUploaderRepairHandler extends AbstractPayloadHandler{

    public DataUploaderRepairHandler(RocketMQTemplate rocketMQTemplate, ICommandService commandService){
        super(rocketMQTemplate,commandService);
    }

    @Override
    public String getTopic() {
        return TopicConstants.TBOX_DATA_REPORTED_REPARIR_TOPIC;
    }

    @Override
    public void handler(MessageProto msg, byte[] payload, ChannelHandlerContext ctx) {
        sendMsgToMq(msg);
    }
}