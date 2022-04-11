package com.elinxer.springcloud.platform.core.config;

import com.elinxer.springcloud.platform.core.handle.RestExceptionTranslatorHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用包的自动装配类 此类指向启动类自动配置
 *
 * src/main/resources/META-INF/spring.factories
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
