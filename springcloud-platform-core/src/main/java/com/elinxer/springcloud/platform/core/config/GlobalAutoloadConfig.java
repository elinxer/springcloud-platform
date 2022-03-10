package com.elinxer.springcloud.platform.core.config;

import com.elinxer.springcloud.platform.core.handle.RestExceptionTranslatorHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用包的自动装配类
 *
 * @author elinx
 */
@Configuration
public class GlobalAutoloadConfig {

    @Bean
    public RestExceptionTranslatorHandle restExceptionTranslator() {
        return new RestExceptionTranslatorHandle();
    }

}
