package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.handler;
/**
 * @description
 * @author elinx
 * @create 2021-10-08 19:41
 **/

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants.TopicConstants;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.EncodeType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgBodyType;
import com.elinxer.springcloud.platform.mqtt.broker.service.ICommandService;
import com.google.protobuf.InvalidProtocolBufferException;
import com.elinxer.springcloud.platform.mqtt.broker.entity.Session;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.body.LoginoutProtoc;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants.ProtocConstants;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants.TopicConstants;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.EncodeType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header.MsgBodyType;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.protoc.MessageProto;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils.ProtocMsgBuild;
import com.elinxer.springcloud.platform.mqtt.broker.service.ICommandService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 *
 */
@TboxPayload(type = MsgBodyType.VEHICLE_LOGIN_IN_OUT)
@Slf4j
public class LoginoutHandler extends AbstractPayloadHandler {

    public LoginoutHandler(RocketMQTemplate rocketMQTemplate, ICommandService commandService) {
        super(rocketMQTemplate,commandService);
    }


    @Override
    public String getTopic() {
        return TopicConstants.TBOX_LOGINOUT_TOPIC;
    }

    @Override
    public void handler(MessageProto msg, byte[] payload, ChannelHandlerContext ctx) {
        EncodeType encodeType = msg.getHeader().getMsgAttr().getEncodeType();
        byte[] bodyMsg = msg.getBodyMsg();
        switch (encodeType) {
            case PROTO3:
                LoginoutProtoc.Loginout loginoutResponse = null;
                try {
                    LoginoutProtoc.Loginout loginout = LoginoutProtoc.Loginout.parseFrom(bodyMsg);
                    log.info("loginout={}", loginout.toString());
                    int businessType = loginout.getBusinessType();
                    int seq = loginout.getLoginoutRequest().getSequenceId();
                    int resBusinessType = businessType == ProtocConstants.LOGIN_REQ ?
                            ProtocConstants.LOGIN_RESP : ProtocConstants.LOGOUT_RESP;
                    loginoutResponse = ProtocMsgBuild.loginout(ProtocConstants.LOGINOUT_SUCESS_STATUS, 0, "成功", seq, resBusinessType);

                    if(ctx!=null){
                        Session session = (Session) ctx.channel().attr(AttributeKey.valueOf(Session.KEY)).get();
                        if(session!=null){
                            session.setLoginStatus(businessType);
                        }
                    }
                    sendMsgToMq(msg.getHeader(),loginoutResponse.toByteArray());
                } catch (InvalidProtocolBufferException e) {
                    log.error("ex ={}", e);
                    loginoutResponse = ProtocMsgBuild.loginout(ProtocConstants.LOGINOUT_FAILED_STATUS, 0, "解析body失败", 0, 0);
                }
                commandService.sendToDevice(msg.getHeader(), loginoutResponse.toByteArray());
                break;
            case JSON:
                log.info("JSON");
                break;
            default:
                break;
        }
    }


}