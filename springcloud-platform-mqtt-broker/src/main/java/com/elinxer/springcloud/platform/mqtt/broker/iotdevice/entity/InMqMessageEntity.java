/**
 * @description
 * @author elinx
 * @create 2021-10-13 14:33
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InMqMessageEntity implements Serializable {
    //设备id
    private String deviceId;

    //设备iD类型
    // VIN((byte) 0x00),
    // IMEI((byte) 0x01),
    // ICCID((byte) 0x02),
    // PHONE((byte) 0x03),
    // SN_CODE((byte) 0x04);
    private byte deviceIdType;

    //接入网联终端类型 1、tbox、2无人机、3无人船
    private short iotDeviceType;

    //序列号
    private Integer seq;

    //版本号
    private Byte version;

    //bodyMsg的序列化方式 1、protoc3，2、json
    private short encodeType;
    //消息体类型
    //x01 登入登出、0x02 远程控制、0x03数据上报、0x04 数据上报补发、0x05 告警信息、0x06远程诊断
    private short bodyMsgType;

    //消息体-原始报文中的消息体
    private byte[] bodyMsg;

    //时间戳 -取自原始报文header中的时间戳
    private long timestampL;
}