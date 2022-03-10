package com.elinxer.springcloud.platform.core.config;

import feign.Logger.Level;
import org.springframework.context.annotation.Bean;


/**
 * Feign全局配置
 */
public class GlobalFeignConfig {

    @Bean
    public Level level() {
        return Level.FULL;
    }

}

