/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-11 17:08
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.IotDeviceType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils.ParseMsgUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class IotMessageDelegatingProcessor {

    private Map<IotDeviceType, AbstractMessageProcessor> processMap = new HashMap<IotDeviceType, AbstractMessageProcessor>();

    public IotMessageDelegatingProcessor(List<AbstractMessageProcessor> processList) {
        Assert.notNull(processList, "processList is not null");
        processList.forEach(abstractMessageProcess -> {
            IotDevice iotDevice = abstractMessageProcess.getClass().getAnnotation(IotDevice.class);
            Optional.ofNullable(iotDevice).map(IotDevice::type)
                    .ifPresent(iotDeviceType -> {
                        processMap.put(iotDeviceType, abstractMessageProcess);
                    });
        });
    }

    public void process(byte[] payload, ChannelHandlerContext ctx) {
        long startTime = System.currentTimeMillis();
        MessageProto msg = ParseMsgUtils.parseMessage(payload);
        AbstractMessageProcessor messageProcess = processMap.get(msg.getHeader().getIotDeviceType());
        if (messageProcess == null) {
            log.info("IotDeviceType={}  messageProcess is null", msg.getHeader().getIotDeviceType());
            return;
        }
        messageProcess.process(msg, payload, ctx);
        long endTime = System.currentTimeMillis();

        log.info("iotMessageDelegatingProcessor-cost-time={},endTime2={},headerTimeCost={}", endTime - startTime, endTime, endTime - msg.getHeader().getTimestampL());

    }
}