/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-11 16:05
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import io.netty.channel.ChannelHandlerContext;

public interface IotMessageProcessor {
    boolean verifyMsg(MessageProto messageProto, byte[] payload);
    void process0(MessageProto messageProto, byte[] payload, boolean successed, ChannelHandlerContext ctx);

}
