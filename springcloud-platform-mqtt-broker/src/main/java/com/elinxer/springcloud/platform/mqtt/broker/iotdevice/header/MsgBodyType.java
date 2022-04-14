package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header;
/**
 * @description 消息体业务类型
 * @author elinx
 * @create 2021-09-30 11:10
 **/


import java.util.Arrays;

public enum MsgBodyType {

    VEHICLE_LOGIN_IN_OUT((short)0x01),

    REMOTE_CTR((short) 0x02),

    DATA_UPLOAD((short)0x03),

    MSG_REISSUING((short) 0x04),

    ALARM_INFO((short)0x05),

    REMOTE_DIAGNOSIS((short) 0x06);

    private short value;

    private MsgBodyType(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public static MsgBodyType of(short value) {

        return Arrays.stream(MsgBodyType.values())
                .filter(msgBodyType -> value==msgBodyType.getValue())
                .findFirst().orElse(null);

    }


}