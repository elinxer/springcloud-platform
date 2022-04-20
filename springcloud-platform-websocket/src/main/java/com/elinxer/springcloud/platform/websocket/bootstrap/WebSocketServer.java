/*
 * Copyright (c) 2021.  广东皓行科技有限公司 All Rights Reserved.FileName:
 * com.hxkj.websocket@author: Elinx<yangdongsheng03>@date: 2021/5/11 16:20@version: 1.0
 * This content is owned by the copyright owner, without any authorization, and is limited to internal circulation, disclosure and other commercial purposes, and shall not be copied or published on any network computer, nor disseminated on any media.Without the permission of the copyright owner, no one may use (including but not limited to: copying, spreading, displaying, mirroring, uploading, downloading) in an illegal way.Otherwise, the copyright owner will investigate its legal liability according to law.
 */
package com.elinxer.springcloud.platform.websocket.bootstrap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.elinxer.springcloud.platform.websocket.protocol.BuildProtocol;
import com.elinxer.springcloud.platform.websocket.protocol.SystemProto;
import com.elinxer.springcloud.platform.websocket.protocol.UpStreamCode;
import com.elinxer.springcloud.platform.websocket.protocol.UpStreamMessage;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnMessage;
import javax.websocket.Session;

/**
 * @author Elinx<yangdongsheng03>
 * @since 2021-05-11 16:20
 */
@Slf4j
//@Component
//@ServerEndpoint(value = "/ws/asset")
public class WebSocketServer extends WebSocketServerBoot {

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @Override
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("===GET WebSocketServer={}", message);
            JSONObject jsonObject = JSON.parseObject(message);
            UpStreamMessage upStreamMessage = JSON.toJavaObject(jsonObject, UpStreamMessage.class);
            switch (upStreamMessage.getCode()) {
                case UpStreamCode.PING_CODE:
                    pushPing(session);
                    break;
                case UpStreamCode.BIND_TOPIC:
                    bindTopicSessionSet(session, upStreamMessage.getTopic());
                    break;
                case UpStreamCode.UNBOUND_TOPIC:
                    unboundTopicSessionSet(session, upStreamMessage.getTopic());
                    break;
                case UpStreamCode.SEND_COMMAND:
                    // TODO 暂时走接口
                    break;
                case UpStreamCode.MACHINE_SUBSCRIBE:

                    // 检查是否具备绑定权限
                    String token = getToken(session);
                    String authType = getAuthType(session);
                    if (token != null && "hx-ykdj-app".equals(authType)) {
                        if (!this.checkTopicValid(upStreamMessage.getTopic(), token)) {
                            bindTopicSessionError(session, upStreamMessage.getTopic());
                        } else {
                            // 绑定topic
                            bindTopicSessionSet(session, upStreamMessage.getTopic());
                        }
                    }

                    // TODO 绑定topic
                    //bindTopicSessionSet(session, parseUpStream.getTopic());

                    //CommandMqttClient.subscribe(parseUpStream.getBody(), SpringContextUtil.getBean(IMqttClientWrapper.class));

                    break;
                case UpStreamCode.MACHINE_UNSUBSCRIBE:
                    //CommandMqttClient.unsubscribe(parseUpStream.getBody(), SpringContextUtil.getBean(IMqttClientWrapper.class));
                    break;
                default:
                    break;
            }
        } catch (JSONException jsonException) {
            log.error("===WebSocketServer.onMessage.JSONException={}", jsonException.getMessage());
        } catch (Exception e) {
            log.error("===WebSocketServer.onMessage.Exception={}", e);
        }
    }

    public void pushPing(Session session) {
        try {
            // 更新session有效期
            updateSessionLive(session);
            // 心跳响应
            String message = BuildProtocol.buildProtocol(SystemProto.HEART_PING, "ping success!");
            SendMessage(message, session.getId());
        } catch (Exception e) {
            log.error("===WebSocketServer.pushPing.error={}", e);
        }
    }

    public boolean checkTopicValid(String topic, String token) {
        return true;
    }
}
