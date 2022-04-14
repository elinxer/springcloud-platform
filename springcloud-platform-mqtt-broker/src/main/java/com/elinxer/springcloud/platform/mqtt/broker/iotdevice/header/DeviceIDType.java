package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header;
/**
 * @description 设备id类型
 * @author elinx
 * @create 2021-09-30 15:28
 **/


import java.util.Arrays;

public enum DeviceIDType {
    VIN((byte) 0x00),
    IMEI((byte) 0x01),
    ICCID((byte) 0x02),
    PHONE((byte) 0x03),
    SN_CODE((byte) 0x04);

    private DeviceIDType(byte value) {
        this.value = value;

    }

    private byte value;

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public static DeviceIDType of(byte value) {
        return Arrays.stream(DeviceIDType.values())
                .filter(deviceIDType -> value==deviceIDType.getValue())
                .findFirst().orElse(null);
    }

}