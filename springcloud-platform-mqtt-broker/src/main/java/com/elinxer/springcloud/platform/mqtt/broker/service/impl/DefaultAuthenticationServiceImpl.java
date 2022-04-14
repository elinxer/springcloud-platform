package com.elinxer.springcloud.platform.mqtt.broker.service.impl;


import com.elinxer.springcloud.platform.mqtt.broker.entity.Authentication;
import com.elinxer.springcloud.platform.mqtt.broker.entity.ClientAuth;
import com.elinxer.springcloud.platform.mqtt.broker.service.IAuthenticationService;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * 认证服务
 */
@Service
public class DefaultAuthenticationServiceImpl implements IAuthenticationService {

    @Override
    public void asyncAuthenticate(ClientAuth authDTO, Consumer<Authentication> onResponse, Consumer<Throwable> onFailure) {
        onResponse.accept(null);
    }
}
