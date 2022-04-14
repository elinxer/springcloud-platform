package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header;
/**
 * @description 接入网联终端类型
 * @author caoxiaoguang
 * @create 2021-09-30 15:19
 **/


import java.util.Arrays;

public enum IotDeviceType {
    TBOX((short) 0x01),//tbox 农机
    UAV((short) 0x02),//无人机
    NMANNED_SHIP((short)0x03); //无人船

    private short value;

    private IotDeviceType(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public static IotDeviceType of(short value) {
        return Arrays.stream(IotDeviceType.values())
                .filter(iotDeviceType -> value==iotDeviceType.getValue())
                .findFirst().orElse(null);
    }
}
