package com.elinxer.springcloud.platform.mqtt.broker.iotdevice;


import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.IotDeviceType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface IotDevice {
    IotDeviceType type();
}
