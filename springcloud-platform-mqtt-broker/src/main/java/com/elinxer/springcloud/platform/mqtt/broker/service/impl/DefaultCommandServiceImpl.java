/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-13 14:59
 **/
package com.elinxer.springcloud.platform.mqtt.broker.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.elinxer.springcloud.platform.mqtt.broker.broker.BrokerHandler;
import com.elinxer.springcloud.platform.mqtt.broker.broker.handler.ConnectHandler;
import com.elinxer.springcloud.platform.mqtt.broker.entity.ClientSub;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants.TopicConstants;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.entity.InMqMessageEntity;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.*;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils.ByteUtils;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils.CRC32Utils;
import com.elinxer.springcloud.platform.mqtt.broker.service.ICommandService;
import com.elinxer.springcloud.platform.mqtt.broker.service.ISubscriptionService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DefaultCommandServiceImpl implements ICommandService {

    private AtomicInteger seqAtomicInteger = new AtomicInteger(0);

    private AtomicInteger packetAtomicInteger = new AtomicInteger(0);

    private ISubscriptionService subscriptionService;

    public DefaultCommandServiceImpl(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void sendToDevice(InMqMessageEntity entity) {
        log.info("sendToDevice-InMqMessageEntity={}", JSONObject.toJSONString(entity));
        byte[] payload = entity.getBodyMsg();
        MsgHeader header = new MsgHeader();
        header.setDeviceID(entity.getDeviceId());
        header.setDeviceIDType(DeviceIDType.of(entity.getDeviceIdType()));
        header.setIotDeviceType(IotDeviceType.of(entity.getIotDeviceType()));
        header.setVersion(entity.getVersion() == null ? (short) 1 : entity.getVersion());
        header.setMsgBodyType(MsgBodyType.REMOTE_CTR);
        MsgHeader.MsgAttr msgAttr = new MsgHeader.MsgAttr(EncryptType.NO_SEC, EncodeType.of(entity.getEncodeType()), ZipType.NONE, CheckCodeType.CRC32);
        header.setMsgAttr(msgAttr);
        header.setSeq(entity.getSeq());
        header.setTimestampL(entity.getTimestampL());
        sendToDevice(header, payload);

    }

    @Override
    public void sendToDevice(MsgHeader header, byte[] payload) {

        String topic = String.format(TopicConstants.PUSH_VEHICLE_CONTROL_TOPIC, header.getDeviceID());
        log.info("sendToDevice-topic={}", topic);
        List<ClientSub> clientSubList = subscriptionService.searchSubscribeClientList(topic);
        if (clientSubList == null || clientSubList.isEmpty()) {
            log.info("sendToDevice-topic={},deviceID={} clientSubList is null ", topic, header.getDeviceID());
            return;
        }
        log.info("sendToDevice-clientSubList={}", clientSubList);
        int msgLenth = payload.length;
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(msgLenth);
        //消息的业务类型
        byteBuf.writeShort(header.getMsgBodyType().getValue());
        //协议版本号
        byteBuf.writeShort(header.getVersion());
        //消息属性，不加密、protoc3，不压缩
        MsgHeader.MsgAttr msgAttr = header.getMsgAttr();
        byteBuf.writeShort(header.getMsgAttrShort(msgAttr));
        byteBuf.writeShort(header.getIotDeviceType().getValue());
        byteBuf.writeByte(header.getDeviceIDType().getValue());
        byte[] deveiceIdT = header.getDeviceID().getBytes(StandardCharsets.UTF_8);
        byte[] deveiceIdB = null;
        if (deveiceIdT.length > 32) {
            deveiceIdB = new byte[32];
            System.arraycopy(deveiceIdT, 0, deveiceIdB, 0, 32);
        } else {
            deveiceIdB = deveiceIdT;
        }
        byteBuf.writeBytes(deveiceIdB);
        for (int i = 0; i < 32 - deveiceIdB.length; i++) {
            byteBuf.writeByte(0);
        }
        byteBuf.writeInt(seqAtomicInteger.getAndIncrement());
        byteBuf.writeLong(System.currentTimeMillis());

        Integer checkCodeLen = null;
        switch (msgAttr.getCheckCodeType()) {
            case CRC32:
                checkCodeLen = 4;
                break;
            case BCC:
                checkCodeLen = 1;
                break;
            default:
                checkCodeLen = 4;
                break;
        }
        byteBuf.writeByte(checkCodeLen);
        byteBuf.writeBytes(payload);

        ByteBuf checkCodeData = byteBuf.copy();
        byte[] data = new byte[checkCodeData.readableBytes()];
        checkCodeData.readBytes(data);
        log.info("buildResponse-msgAttr={}", msgAttr);
        switch (msgAttr.getCheckCodeType()) {
            case CRC32:
                Long checkcode = CRC32Utils.getCRC32(data);
                log.info("buildResponse-checkcode={}", checkcode);
                byte[] longToByteArray = ByteUtils.longToByteArray(checkcode);
                byte[] intArray = new byte[4];
                System.arraycopy(longToByteArray, 4, intArray, 0, 4);
                byteBuf.writeBytes(intArray);
                break;
            case BCC:
                byte checkCode = 1;
                byteBuf.writeByte(checkCode);
                break;
            default:
                log.info("default-CheckCodeType={}", msgAttr.getCheckCodeType());
                Long checkcodeD = CRC32Utils.getCRC32(data);
                log.info("buildResponse-checkcode={}", checkcodeD);
                byte[] longToByteArrayD = ByteUtils.longToByteArray(checkcodeD);
                byte[] intArrayD = new byte[4];
                System.arraycopy(longToByteArrayD, 4, intArrayD, 0, 4);
                byteBuf.writeBytes(intArrayD);
                break;
        }

        clientSubList.forEach(clientSub -> {
            Optional.ofNullable(ConnectHandler.CLIENT_MAP.get(clientSub.getClientId()))
                    .map(BrokerHandler.CHANNELS::find).ifPresent(channel -> {
                MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0),
                        new MqttPublishVariableHeader(topic, packetAtomicInteger.getAndIncrement()),
                        byteBuf.retain());
                log.info("send-sendToDevice clientId={},topic={}", clientSub.getClientId(), topic);
                channel.writeAndFlush(mqttPublishMessage);
            });
        });
        checkCodeData.release();
        byteBuf.release();
    }

}