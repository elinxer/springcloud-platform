/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-11 16:11
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import io.netty.channel.ChannelHandlerContext;


public abstract class AbstractMessageProcessor implements IotMessageProcessor {

    public void process(MessageProto messageProto, byte[] payload, ChannelHandlerContext ctx) {
        boolean successed = verifyMsg(messageProto, payload);
        process0(messageProto, payload, successed,ctx);
    }
}