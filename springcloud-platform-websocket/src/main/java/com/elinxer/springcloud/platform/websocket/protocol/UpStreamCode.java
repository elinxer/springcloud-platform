/*
 * Copyright (c) 2021.  广东皓行科技有限公司 All Rights Reserved.FileName:
 * com.hxkj.websocket.protocol@author: Elinx<yangdongsheng03>@date: 2021/5/14 10:45@version: 1.0
 * This content is owned by the copyright owner, without any authorization, and is limited to internal circulation, disclosure and other commercial purposes, and shall not be copied or published on any network computer, nor disseminated on any media.Without the permission of the copyright owner, no one may use (including but not limited to: copying, spreading, displaying, mirroring, uploading, downloading) in an illegal way.Otherwise, the copyright owner will investigate its legal liability according to law.
 */
package com.elinxer.springcloud.platform.websocket.protocol;

/**
 * @author Elinx<yangdongsheng03>
 * @since 2021-10-14 10:45
 */

public class UpStreamCode {

    /**
     * PING
     */
    public static final int PING_CODE = 10000;

    /**
     * 绑定主题
     */
    public static final int BIND_TOPIC = 10001;

    /**
     * 解绑主题
     */
    public static final int UNBOUND_TOPIC = 10002;

    /**
     * 下发指令
     */
    public static final int SEND_COMMAND = 10003;

    /**
     * 订阅设备消息
     */
    public static final int MACHINE_SUBSCRIBE = 10005;

    /**
     * 取消订阅消息
     */
    public static final int MACHINE_UNSUBSCRIBE = 10006;


}
