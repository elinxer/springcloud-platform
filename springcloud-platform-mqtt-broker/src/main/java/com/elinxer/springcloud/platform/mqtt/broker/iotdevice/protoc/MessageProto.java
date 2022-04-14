/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-08 15:26
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc;


import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgHeader;
import lombok.Data;

@Data
public class MessageProto {

    private MsgHeader header;
    private byte[] bodyMsg;
    private Long checkCode;

    public static MessageProto of(MsgHeader header, byte[] bodyMsg, Long checkCode) {
        MessageProto messageProto = new MessageProto();
        messageProto.setBodyMsg(bodyMsg);
        messageProto.setCheckCode(checkCode);
        messageProto.setHeader(header);
        return messageProto;
    }

}