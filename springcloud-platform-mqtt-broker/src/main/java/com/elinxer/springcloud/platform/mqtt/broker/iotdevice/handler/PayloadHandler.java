package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler;
/**
 * @description mqtt 有效负载处理器
 * @author elinx
 * @create 2021-10-08 17:51
 **/

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import io.netty.channel.ChannelHandlerContext;

public interface PayloadHandler {
   public void handler(MessageProto msg, byte[] payload, ChannelHandlerContext ctx);
}