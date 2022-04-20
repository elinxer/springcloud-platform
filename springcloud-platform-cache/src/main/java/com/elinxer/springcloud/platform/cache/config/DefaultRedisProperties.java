package com.elinxer.springcloud.platform.cache.config;

import com.elinxer.springcloud.platform.cache.serializer.SerializerType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis自定义配置类
 *
 * @author elinx
 * @version 3.0.0
 */
@Data
@ConfigurationProperties(prefix = DefaultRedisProperties.PREFIX)
public class DefaultRedisProperties {

    public static final String PREFIX = "platform.redis";

    public boolean enable = true;

    private SerializerType serializerType = SerializerType.JACK_SON;


}




