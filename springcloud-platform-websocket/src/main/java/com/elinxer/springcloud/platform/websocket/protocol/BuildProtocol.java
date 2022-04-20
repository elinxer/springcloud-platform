/*
 * Copyright (c) 2021.  广东皓行科技有限公司 All Rights Reserved.FileName:
 * com.hxkj.websocket.protocol@author: Elinx<yangdongsheng03>@date: 2021/5/14 10:45@version: 1.0
 * This content is owned by the copyright owner, without any authorization, and is limited to internal circulation, disclosure and other commercial purposes, and shall not be copied or published on any network computer, nor disseminated on any media.Without the permission of the copyright owner, no one may use (including but not limited to: copying, spreading, displaying, mirroring, uploading, downloading) in an illegal way.Otherwise, the copyright owner will investigate its legal liability according to law.
 */
package com.elinxer.springcloud.platform.websocket.protocol;

import com.alibaba.fastjson.JSONObject;
import com.elinxer.springcloud.platform.websocket.domain.MqMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 构建协议结构体
 *
 * @author Elinx<yangdongsheng03>
 * @since 2021-05-14 10:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildProtocol {

    /**
     * 消息编码
     */
    public int uri;

    /**
     * 消息体
     */
    public Object body;

    /**
     * 拓展结构体
     */
    public Object extend;

    /**
     * 消息主题
     */
    public String topic;

    /**
     * 当前时间戳
     */
    public Long timestamp;

    /**
     * 构建初始化信息
     *
     * @param uri  协议
     * @param body 消息体
     */
    public BuildProtocol(int uri, Object body) {
        this.uri = uri;
        this.body = body;
        this.timestamp = System.currentTimeMillis();
    }

    public static String buildProtocol(int uri, Object body) {
        BuildProtocol buildProtocol = new BuildProtocol(uri, body);
        return JSONObject.toJSONString(buildProtocol);
    }

    /**
     * 构建协议内容并指定topic
     *
     * @param uri   topic 下协议
     * @param body  协议内容
     * @param topic 主题
     * @return String
     */
    public static String buildProtocol(int uri, Object body, String topic) {
        BuildProtocol buildProtocol = new BuildProtocol(uri, body);
        buildProtocol.topic = topic;
        return JSONObject.toJSONString(buildProtocol);
    }

    /**
     * 构建协议内容并指定topic
     *
     * @param mqMessage 指定消息体
     * @return String
     */
    public static String buildProtocol(MqMessage mqMessage) {
        BuildProtocol buildProtocol = new BuildProtocol(mqMessage.getUri(), mqMessage.getBody());
        buildProtocol.topic = mqMessage.getTopic();
        return JSONObject.toJSONString(buildProtocol);
    }


}
