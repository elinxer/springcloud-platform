package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils;
/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-08 9:54
 **/


import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.DeviceIDType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.IotDeviceType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgBodyType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgHeader;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ParseMsgUtils {

    public static MessageProto parseMessage(byte[] playLoaded) {
        //消息长度
        long startTime =System.currentTimeMillis();

        log.info("payload={}", playLoaded);
        ByteBuf byteBuf = Unpooled.buffer(playLoaded.length);
        byteBuf.writeBytes(playLoaded);
        MsgHeader msgHeader = new MsgHeader();

        int msgBodyLen = byteBuf.readInt();
        log.info("msgBodyLen={}", msgBodyLen);

        short msgBodyType = byteBuf.readShort();
        log.info("msgBodyType={}", msgBodyType);

        short version = byteBuf.readShort();
        log.info("version={}", version);

        short msgAttrShort = byteBuf.readShort();
        log.info("msgAttr={}", msgAttrShort);
        MsgHeader.MsgAttr msgAttr=msgHeader.of(msgAttrShort);


        short iotDeviceType = byteBuf.readShort();
        log.info("iotDeviceType={}", iotDeviceType);


        byte deviceIDType = byteBuf.readByte();
        log.info("deviceIDType={}", deviceIDType);

        byte[] deviceIdB = new byte[32];
        byteBuf.readBytes(deviceIdB);
       /* int index = -1;
        int len = deviceIdB.length;
        for (int i = 0; i < len; i++) {
            byte b = deviceIdB[i];
            if (b == 0x00) {
                index = i;
            } else {
                break;
            }
        }
        byte[] deviceIDBCopy = new byte[len - (index + 1)];
        System.arraycopy(deviceIdB, index + 1, deviceIDBCopy, 0, len - (index + 1));*/
        String deviceId = new String(deviceIdB);
        deviceId= deviceId.replaceAll("\\u0000","");
        log.info("deviceId={}", deviceId);

        int seq = byteBuf.readInt();
        log.info("seq={}", seq);

        long timestamp = byteBuf.readLong();
        log.info("timestamp={}", timestamp);

        byte checkCodeLen = byteBuf.readByte();
        log.info("checkCodeLen={}", checkCodeLen);

        byte[] msgBodyB = new byte[msgBodyLen];
        byteBuf.readBytes(msgBodyB);
        //
        Long checkCode=null;
        switch (msgAttr.getCheckCodeType()){
            case CRC32:
                byte[] checkCodeB = new byte[checkCodeLen];
                byteBuf.readBytes(checkCodeB);
                checkCode = ByteUtils.byteArrayToLong(checkCodeB);
                log.info("checkCode={}", checkCode);
                break;
            case BCC:
                checkCode=Long.valueOf(byteBuf.readByte());
                break;
            default:
                log.info("default====== ");
                byte[] checkCodeD = new byte[checkCodeLen];
                byteBuf.readBytes(checkCodeD);
                checkCode =ByteUtils.byteArrayToLong(checkCodeD);
                log.info("checkCode={}", checkCode);
                break;
        }

        byte[]verifyData =new byte[playLoaded.length-checkCodeLen];

        System.arraycopy(playLoaded,0,verifyData,0,playLoaded.length-checkCodeLen);
        Long computeCode =CRC32Utils.getCRC32(verifyData);
        log.info("computeCode={}",computeCode);

        msgHeader.setMsgBodyLenth(msgBodyLen);
        MsgBodyType msgBodyTypeEnum = MsgBodyType.of(msgBodyType);
        Assert.notNull(msgBodyTypeEnum, "msgBodyType=" + msgBodyType + "is not null");
        msgHeader.setMsgBodyType(msgBodyTypeEnum);

        msgHeader.setVersion(version);
        msgHeader.setMsgAttr(msgAttr);

        IotDeviceType iotDeviceTypeEnum = IotDeviceType.of(iotDeviceType);
        Assert.notNull(iotDeviceTypeEnum, "iotDeviceTypeEnum=" + iotDeviceType + "is not null");
        msgHeader.setIotDeviceType(iotDeviceTypeEnum);

        DeviceIDType deviceIDTypeEnum = DeviceIDType.of(deviceIDType);
        Assert.notNull(deviceIDTypeEnum, "deviceIDTypeEnum=" + deviceIDType + "is not null");
        msgHeader.setDeviceIDType(deviceIDTypeEnum);

        msgHeader.setDeviceID(deviceId);

        msgHeader.setSeq(seq);

        msgHeader.setTimestampL(timestamp);

        msgHeader.setCheckCodeLen(checkCodeLen);

        MessageProto messageProto = MessageProto.of(msgHeader, msgBodyB, checkCode);
        byteBuf.release();
        long endTime =System.currentTimeMillis();
        log.info("parseMessage-costTime={}",endTime-startTime);
        return messageProto;
    }


    public static void main(String[] args) {

        //消息长度
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(55);
        //消息的业务类型
        byteBuf.writeShort(1);
        //协议版本号
        byteBuf.writeShort(1);
        //消息属性，不加密、protoc3，不压缩
        byteBuf.writeShort(32);

        //接入网联终端类型  01车联网终端接入:TBOX
        byteBuf.writeShort(1);
        //设备ID类型 IME
        byteBuf.writeByte(1);
        //设备id
        String deveciId = "test13233";
        System.out.println("1=" + byteBuf.readableBytes());
        byte[] deveiceIdB = deveciId.getBytes(StandardCharsets.UTF_8);
        System.out.println(deveiceIdB.length);
        for (int i = 0; i < 20 - deveiceIdB.length; i++) {
            byteBuf.writeByte(0);
        }
        System.out.println("2=" + byteBuf.readableBytes());
        byteBuf.writeBytes(deveiceIdB);
        System.out.println("3=" + byteBuf.readableBytes());
        // 序列号
        byteBuf.writeInt(9);
        //时间戳
        byteBuf.writeLong(System.currentTimeMillis());
        //校验码长度
        byteBuf.writeByte(7);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        parseMessage(bytes);


    }
}