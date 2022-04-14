package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler; /**
 * @description
 * @author elinx
 * @create 2021-10-08 19:42
 **/


import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgBodyType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface TboxPayload {
    MsgBodyType type();

}
