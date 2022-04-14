package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header;
/**
 * @description 编码方式
 * @author caoxiaoguang
 * @create 2021-09-30 14:55
 **/


import java.util.Arrays;


public enum EncodeType {
    NONE((short) 0x00),
    PROTO3((short) 0x01),
    JSON((short) 0x02);

    private short value;

    private EncodeType(short value){
        this.value=value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public static EncodeType of(short value) {
        return Arrays.stream(EncodeType.values())
                .filter(encodeType -> value==encodeType.getValue())
                .findFirst().orElse(null);
    }
}