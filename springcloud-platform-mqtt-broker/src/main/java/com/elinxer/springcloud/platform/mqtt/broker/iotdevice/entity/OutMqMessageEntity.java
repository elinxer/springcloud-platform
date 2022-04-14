/**
 * @description 数据上报到mq的消息体格式
 * @author caoxiaoguang
 * @create 2021-10-11 20:34
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.entity;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgHeader;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutMqMessageEntity implements Serializable {

    //设备id--取自原始报文header中的设备id
    private String deviceId;

    //VIN((byte) 0x00),
    //IMEI((byte) 0x01),
    //ICCID((byte) 0x02),
    //PHONE((byte) 0x03),
    //SN_CODE((byte) 0x04);
    private byte deviceIdType;

    //接入网联终端类型 1、tbox、2无人机、3无人船
    private short iotDeviceType;

    //bodyMsg的序列化方式 1、protoc3，2、json
    private short encodeType;

    //消息体类型
    //x01 登入登出、0x02 远程控制、0x03数据上报、0x04 数据上报补发、0x05 告警信息、0x06远程诊断
    private short bodyMsgType;

    //消息流水号 消息序列号
    private Integer seq;

    //消息体-原始报文中的消息体
    private byte[] bodyMsg;

    //时间戳 -取自原始报文header中的时间戳
    private long timestampL;

    private short version;


    public static OutMqMessageEntity of(MessageProto msg){
        OutMqMessageEntity entity = OutMqMessageEntity.builder()
                .deviceId(msg.getHeader().getDeviceID())
                .deviceIdType(msg.getHeader().getDeviceIDType().getValue())
                .iotDeviceType(msg.getHeader().getIotDeviceType().getValue())
                .encodeType(msg.getHeader().getMsgAttr().getEncodeType().getValue())
                .seq(msg.getHeader().getSeq())
                .bodyMsgType(msg.getHeader().getMsgBodyType().getValue())
                .bodyMsg(msg.getBodyMsg()).version(msg.getHeader().getVersion())
                .timestampL(msg.getHeader().getTimestampL())
                .build();
        return  entity;
    }

    public static OutMqMessageEntity of(byte[]payload, MsgHeader header){
        OutMqMessageEntity entity = OutMqMessageEntity.builder()
                .deviceId(header.getDeviceID())
                .deviceIdType(header.getDeviceIDType().getValue())
                .iotDeviceType(header.getIotDeviceType().getValue())
                .encodeType(header.getMsgAttr().getEncodeType().getValue())
                .seq(header.getSeq())
                .bodyMsgType(header.getMsgBodyType().getValue())
                .bodyMsg(payload)
                .timestampL(header.getTimestampL())
                .build();
        return  entity;
    }


}