/**
 * @description
 * @author elinx
 * @create 2021-10-13 11:00
 **/
package com.elinxer.springcloud.platform.mqtt.broker.service;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.entity.InMqMessageEntity;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgHeader;

public interface ICommandService {
    public void sendToDevice(InMqMessageEntity entity);

    public void sendToDevice(MsgHeader header, byte[] payload);
}
