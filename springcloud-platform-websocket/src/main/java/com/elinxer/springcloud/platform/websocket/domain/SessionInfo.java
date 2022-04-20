/*
 * Copyright (c) 2021.  广东皓行科技有限公司 All Rights Reserved.FileName:
 * com.hxkj.websocket.entities@author: zhangzezheng@date: 2021-04-29 17:26:05@version: 1.0
 * This content is owned by the copyright owner, without any authorization, and is limited to internal circulation, disclosure and other commercial purposes, and shall not be copied or published on any network computer, nor disseminated on any media.Without the permission of the copyright owner, no one may use (including but not limited to: copying, spreading, displaying, mirroring, uploading, downloading) in an illegal way.Otherwise, the copyright owner will investigate its legal liability according to law.
 */
package com.elinxer.springcloud.platform.websocket.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Elinx<yangdongsheng03>
 * @since 2021-10-13 16:20
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class SessionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Session session;

    private Long timestamp;

    private Set<String> topics;

    private Long userId;

}