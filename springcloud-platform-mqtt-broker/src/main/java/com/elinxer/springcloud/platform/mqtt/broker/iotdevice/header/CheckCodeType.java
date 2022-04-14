package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header;
/**
 * @description
 * @author elinx
 * @create 2021-09-30 15:08
 **/

import java.util.Arrays;

/**
 * 校验码类型
 */
public enum CheckCodeType {
    NONE((byte) 0x00),
    BCC((byte)0x01),
    CRC32((byte)0x02);

    private byte value;

    private CheckCodeType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public static CheckCodeType of(byte value) {
        return Arrays.stream(CheckCodeType.values())
                .filter(checkCodeType -> value==checkCodeType.getValue())
                .findFirst().orElse(null);
    }
}
