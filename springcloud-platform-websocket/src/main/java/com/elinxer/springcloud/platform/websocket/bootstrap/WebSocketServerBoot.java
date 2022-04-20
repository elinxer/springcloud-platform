/*
 * Copyright (c) 2021.  广东皓行科技有限公司 All Rights Reserved.FileName:
 * com.hxkj.websocket.core@author: Elinx<yangdongsheng03>@date: 2021/5/11 15:37@version: 1.0
 * This content is owned by the copyright owner, without any authorization, and is limited to internal circulation, disclosure and other commercial purposes, and shall not be copied or published on any network computer, nor disseminated on any media.Without the permission of the copyright owner, no one may use (including but not limited to: copying, spreading, displaying, mirroring, uploading, downloading) in an illegal way.Otherwise, the copyright owner will investigate its legal liability according to law.
 */
package com.elinxer.springcloud.platform.websocket.bootstrap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elinxer.springcloud.platform.websocket.domain.MqMessage;
import com.elinxer.springcloud.platform.websocket.domain.SessionInfo;
import com.elinxer.springcloud.platform.websocket.domain.SysMessage;
import com.elinxer.springcloud.platform.websocket.protocol.SystemProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1.Redis缓存键自定义项目前缀
 * 2.通道管理
 * 3.用户连接ID管理
 * 4.访问鉴权
 *
 * @author Elinx<yangdongsheng03>
 * @since 2021-05-11 15:37
 */
//@Component
//@ServerEndpoint(value = "/ws/asset")
public class WebSocketServerBoot {

    private static Logger log = LoggerFactory.getLogger(WebSocketServerBoot.class);

    private static final AtomicInteger OnlineCount = new AtomicInteger(0);

    /**
     * session生命周期 ： 3分钟
     */
    public static final Integer liveTime = 180000;

    @PostConstruct
    public void init() {
        log.info("websockets loading...");
    }

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
     */
    private static CopyOnWriteArraySet<SessionInfo> sessionSet = new CopyOnWriteArraySet<SessionInfo>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        try {
            Long userId = null;

            try {
                Map<String, List<String>> parameterMap = (Map<String, List<String>>) config.getUserProperties().get("parameterMap");
                if (parameterMap != null) {
//                    LoginAppUser loginAppUser = SysUserUtils.getLoginAppUser(parameterMap);
//                    userId = loginAppUser != null ? loginAppUser.getId() : 0L;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SessionInfo sessionInfo = SessionInfo.builder().session(session)
                    .timestamp(System.currentTimeMillis())
                    .topics(new HashSet<>())
                    .userId(userId)
                    .build();

            log.info("有新连接加入：{}，当前连接数为：{}", session.getId(), OnlineCount.incrementAndGet());

            SendMessage(session, "{\"body\":\"websocket connected.\"}");

            // 初始化时绑定指定Topic
            List<String> topicList = session.getRequestParameterMap().get("topic");
            String topic = topicList != null ? topicList.get(0) : "";
            if (topic != null && !"".equals(topic)) {
                bindTopicSessionSet(session, topic);
            }

            sessionSet.add(sessionInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        try {
            closeSession(session);
            int cnt = OnlineCount.decrementAndGet();
            log.info("有连接关闭，当前连接数为：{}", cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端的消息：{}", message);
            SendMessage(session, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 出现错误
     *
     * @param session s
     * @param error   e
     */
    @OnError
    public void onError(Session session, Throwable error) {
        try {
            toCloseSession(session);
            closeSession(session);
        } catch (Exception e) {
            log.error("发生错误，关闭失败：{}，Session ID： {}", error.getMessage(), session.getId());
            error.printStackTrace();
        }
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    //region Message

    /**
     * 绑定指定Topic
     *
     * @param session Session
     * @param topic   TaskId
     */
    public static void bindTopicSessionSet(Session session, String topic) {
        try {
            if (session != null && topic != null && !"".equals(topic)) {
                for (SessionInfo sessionInfo : sessionSet) {
                    if (sessionInfo == null || sessionInfo.getSession() == null) {
                        continue;
                    }
                    Session bindSession = sessionInfo.getSession();
                    if (bindSession.getId().equals(session.getId()) && bindSession.isOpen()) {
                        Set<String> topics = sessionInfo.getTopics().size() >= 1 ? sessionInfo.getTopics() : new HashSet<>();
                        topics.add(topic);
                        sessionInfo.setTopics(topics);
                    }
                }
                SendMessage(session, "{\"body\":\"Subscription succeeded.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解绑指定Topic
     *
     * @param session Session
     * @param topic   TaskId
     */
    public static void unboundTopicSessionSet(Session session, String topic) {
        try {
            if (session != null && topic != null && !"".equals(topic)) {
                for (SessionInfo sessionInfo : sessionSet) {
                    if (sessionInfo == null || sessionInfo.getSession() == null) {
                        continue;
                    }
                    Session bindSession = sessionInfo.getSession();
                    if (bindSession.getId().equals(session.getId()) && bindSession.isOpen()) {
                        Set<String> topics = sessionInfo.getTopics().size() >= 1 ? sessionInfo.getTopics() : new HashSet<>();
                        topics.remove(topic);
                        sessionInfo.setTopics(topics);
                    }
                }
                SendMessage(session, "{\"body\":\"Unbound succeeded.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定失败消息下发
     *
     * @param session Session
     * @param topic   String
     */
    public static void bindTopicSessionError(Session session, String topic) {
        try {
            MqMessage mqMessage = MqMessage.builder().uri(SystemProto.BIND_TOPIC)
                    .body(SysMessage.builder().code(500).message("绑定主题失败！").build())
                    .build();
            SendMessage(session, JSON.toJSONString(mqMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param session s
     * @param message m
     */
    public static void SendMessage(Session session, String message) throws IOException {
        synchronized (session) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 群发消息
     *
     * @param message 消息
     */
    public static void broadCastInfo(String message) {
        try {
            for (SessionInfo sessionInfo : sessionSet) {
                if (sessionInfo != null && sessionInfo.getSession().isOpen()) {
                    SendMessage(sessionInfo.getSession(), message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按按作业ID群发
     *
     * @param message 消息内容
     */
    public static void broadCastInfoTopic(String message, String topic) {
        try {
            for (SessionInfo sessionInfo : sessionSet) {
                if (sessionInfo == null || sessionInfo.getSession() == null || sessionInfo.getTopics() == null) {
                    continue;
                }
                Session session = sessionInfo.getSession();
                Set<String> topics = sessionInfo.getTopics();
                if (topics != null && topics.contains(topic) && session.isOpen()) {
                    SendMessage(session, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息下发中心
     *
     * @param message 消息内容
     */
    public static void broadCastInfoCentral(String message) {
        try {

            JSONObject broadcast = JSON.parseObject(message);
            MqMessage protocol = JSON.toJavaObject(broadcast, MqMessage.class);

            String topic = protocol.getTopic();
            if (topic != null && !"".equals(topic)) {
                // 按Topic下发
                WebSocketServerBoot.broadCastInfoTopic(message, topic);
            } else {
                // 全局全系统消息
                WebSocketServerBoot.broadCastInfo(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("WebSocketServerBoot.broadCastInfoCentral.error={}", message);
        }
    }

    /**
     * 指定Session发送消息
     *
     * @param sessionId s
     * @param message   m
     */
    public static void SendMessage(String message, String sessionId) throws IOException {
        Session session = null;
        for (SessionInfo sessionInfo : sessionSet) {
            if (sessionInfo.getSession().getId().equals(sessionId)) {
                session = sessionInfo.getSession();
                break;
            }
        }
        if (session != null) {
            SendMessage(session, message);
        } else {
            log.warn("没有找到你指定ID的会话：{}", sessionId);
        }
    }

    /**
     * 主动下线会话
     *
     * @param session s
     */
    private static void toCloseSession(Session session) throws IOException {
        // 判断当前连接是否还在线
        if (session.isOpen()) {
            CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "no response!");
            session.close(closeReason);
        }
    }

    /**
     * 更新session生命周期 - 心跳
     *
     * @param session s
     */
    public static void updateSessionLive(Session session) {
        try {
            // 判断当前连接是否还在线
            if (session.isOpen()) {
                for (SessionInfo sessionInfo : sessionSet) {
                    if (sessionInfo.getSession().getId().equals(session.getId())) {
                        sessionInfo.setTimestamp(System.currentTimeMillis());
                    }
                }
            } else {
                log.info("update error, device offline: {}", session.toString());
            }
        } catch (Exception e) {
            log.info("update error：{}", e.getMessage());
        }
    }

    /**
     * 定时清除无效session 每分钟检查
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public static void sendHeartAll() {
        try {
            String heartMessage = "{\"body\":\"need heart response.\", \"uri\": 476}";
            log.info(heartMessage);
            for (SessionInfo sessionInfo : sessionSet) {
                try {
                    // 主动清除无心跳session
                    if (sessionInfo.getTimestamp() + liveTime <= System.currentTimeMillis()) {
                        log.error("关闭Session: {}", sessionInfo.getSession().getId());
                        String heartError = "{\"body\":\"heart error.\", \"uri\": 732}";
                        SendMessage(heartError, sessionInfo.getSession().getId());
                        toCloseSession(sessionInfo.getSession());
                        closeSession(sessionInfo.getSession());
                        continue;
                    } else {
                        //Long range = System.currentTimeMillis()-(sessionInfo.getTimestamp() + liveTime);
                        //log.info("===liveTim={}, range={}", liveTime, range);
                    }
                } catch (Exception e) {
                    log.error("关闭Session失败: {}", e.getMessage());
                }

                // 发送消息生命周期告警
                try {
                    SendMessage(heartMessage, sessionInfo.getSession().getId());
                } catch (Exception e) {
                    log.error("发送告警消息失败,删除session: " + sessionInfo.getSession().getId());
                    try {
                        closeSession(sessionInfo.getSession());
                        int cnt = OnlineCount.decrementAndGet();
                        log.info("主动连接关闭，当前连接数为：{}", cnt);
                    } catch (Exception e1) {
                        log.error("根据sessionId删除失败");
                        //sessionSet.clear();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * push close session
     *
     * @param s session
     */
    private static void closeSession(Session s) {
        try {
            if (!sessionSet.isEmpty()) {
                sessionSet.removeIf(sessionInfo -> sessionInfo.getSession().getId().equals(s.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    /**
     * 获取token
     *
     * @param session Session
     * @return String
     */
    public static String getToken(Session session) {
        List<String> tokenArr = session.getRequestParameterMap().get("token");
        return tokenArr != null ? tokenArr.get(0) : null;
    }

    /**
     * 获取鉴权检验类型
     *
     * @param session Session
     * @return String
     */
    public static String getAuthType(Session session) {
        List<String> tokenArr = session.getRequestParameterMap().get("auth-type");
        return tokenArr != null ? tokenArr.get(0) : null;
    }

}
