package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header;
/**
 * @description 加密方式
 * @author elinx
 * @create 2021-09-30 14:35
 **/


import java.util.Arrays;

public enum EncryptType  {

    NO_SEC((short) 0x00),
    AES((short) 0x01),
    DES((short) 0x02),
    RSA((short) 0x03)
    ;
    public short value;

    private EncryptType(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public static EncryptType of(short value) {
        return Arrays.stream(EncryptType.values())
                .filter(encryptType -> value==encryptType.getValue())
                .findFirst().orElse(null);
    }
}
