/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-11 16:04
 **/
package com.elinxer.springcloud.platform.mqtt.broker.iotdevice;

import com.alibaba.fastjson.JSONObject;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler.PayloadHandler;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler.TboxPayload;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.CheckCodeType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.IotDeviceType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgBodyType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgHeader;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils.CRC32Utils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * tbox消息处理
 */
@IotDevice(type = IotDeviceType.TBOX)
@Slf4j
public class TboxMessageProcessor extends AbstractMessageProcessor {

    private Map<MsgBodyType, PayloadHandler> payloadHandlerMap = new HashMap<>();

    public TboxMessageProcessor(List<PayloadHandler> handlerList){

        Assert.notNull(handlerList,"processList is not null");
        handlerList.forEach(payloadHandler -> {
            TboxPayload iotDevice=  payloadHandler.getClass().getAnnotation(TboxPayload.class);
            Optional.ofNullable(iotDevice).map(TboxPayload::type)
                    .ifPresent(iotDeviceType -> payloadHandlerMap.put(iotDeviceType, payloadHandler));
        });

    }

    @Override
    public boolean verifyMsg(MessageProto msg, byte[] payload) {
        MsgHeader header = msg.getHeader();
        Assert.notNull(header, "header is not null");
        MsgHeader.MsgAttr msgAttr =header.getMsgAttr();
        Assert.notNull(msgAttr, "msgAttr is not null");
        CheckCodeType type =msgAttr.getCheckCodeType();
        switch (type){
            case CRC32:
                int headerLen =58;
                int checkTotalLen =headerLen+header.getMsgBodyLenth();
                byte []checkBytes =new byte[checkTotalLen];
                System.arraycopy(payload,0,checkBytes,0,checkTotalLen);
                boolean checked = CRC32Utils.verify(msg.getCheckCode(),checkBytes);
                log.info("CRC32-checked={},checkCode={}",checked,msg.getCheckCode());
                return checked;
            case BCC:
                log.info("verify BCC");
                break;
            case NONE:
                log.info("verify NONE");
                break;
            default:break;
        }
        return  true;
    }

    @Override
    public void process0(MessageProto messageProto, byte[] payload, boolean success, ChannelHandlerContext ctx) {
        log.info("verifyMsg sucess={}",success);
        if(success){

            MsgHeader header = messageProto.getHeader();
            log.info("===header={}",JSONObject.toJSONString(header));
            PayloadHandler payloadHandler = payloadHandlerMap.get(header.getMsgBodyType());
            if (payloadHandler == null) {
                log.info("payloadHandler={},not find payloadHandler", JSONObject.toJSONString(header));
                return ;
            }
            payloadHandler.handler(messageProto, payload,ctx);
        }
    }
}