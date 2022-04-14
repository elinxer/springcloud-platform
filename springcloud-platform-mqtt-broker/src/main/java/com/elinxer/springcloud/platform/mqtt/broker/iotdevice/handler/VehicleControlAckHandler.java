/**
 * @description
 * @author caoxiaoguang
 * @create 2021-12-15 15:53
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants.TopicConstants;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgBodyType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import com.elinxer.springcloud.platform.mqtt.broker.service.ICommandService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

@TboxPayload(type = MsgBodyType.REMOTE_CTR)
public class VehicleControlAckHandler extends AbstractPayloadHandler {

    public VehicleControlAckHandler(RocketMQTemplate rocketMQTemplate, ICommandService commandService) {
        super(rocketMQTemplate, commandService);
    }

    @Override
    public String getTopic() {
        return TopicConstants.TBOX_VECHICLE_CONTROL_ACK_TOPIC;
    }

    @Override
    public void handler(MessageProto msg, byte[] payload, ChannelHandlerContext ctx) {
         sendMsgToMq(msg);
    }
}