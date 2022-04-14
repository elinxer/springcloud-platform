package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header; /**
 * @description 消息压缩方式
 * @author caoxiaoguang
 * @create 2021-09-30 15:02
 **/


import java.util.Arrays;


public enum ZipType {
    NONE((short) 0x00),
    GZIP((short) 0x01)
    ;
    private short value;

    private ZipType(short value){
      this.value=value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public static ZipType of(short value) {
        return Arrays.stream(ZipType.values())
                .filter(zipType -> value==zipType.getValue())
                .findFirst().orElse(null);
    }

}
