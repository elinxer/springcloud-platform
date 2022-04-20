/*
 * Copyright (c) 2021.  广东皓行科技有限公司 All Rights Reserved.FileName:
 * com.hxkj.websocket.protocol@author: Elinx<yangdongsheng03>@date: 2021/5/14 10:45@version: 1.0
 * This content is owned by the copyright owner, without any authorization, and is limited to internal circulation, disclosure and other commercial purposes, and shall not be copied or published on any network computer, nor disseminated on any media.Without the permission of the copyright owner, no one may use (including but not limited to: copying, spreading, displaying, mirroring, uploading, downloading) in an illegal way.Otherwise, the copyright owner will investigate its legal liability according to law.
 */
package com.elinxer.springcloud.platform.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Elinx<yangdongsheng03>
 * @since 2021-10-14 10:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqMessage implements Serializable {

    private static final long serialVersionUID = 1L;


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

}
