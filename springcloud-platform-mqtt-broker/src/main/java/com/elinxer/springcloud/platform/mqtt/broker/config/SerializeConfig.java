package com.elinxer.springcloud.platform.mqtt.broker.config;

import com.elinxer.springcloud.platform.mqtt.broker.constants.SerializeStrategy;
import com.elinxer.springcloud.platform.mqtt.broker.utils.JsonSerializer;
import com.elinxer.springcloud.platform.mqtt.broker.utils.KryoSerializer;
import com.elinxer.springcloud.platform.mqtt.broker.utils.ProtobufSerializer;
import com.elinxer.springcloud.platform.mqtt.broker.utils.Serializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializeConfig {

    @Bean
    @ConditionalOnProperty(prefix = "com.hx.broker", name = "serialize-strategy", havingValue = SerializeStrategy.JSON)
    public Serializer jsonSerializer() {
        return new JsonSerializer();
    }

    @Bean
    @ConditionalOnProperty(prefix = "com.hx.broker", name = "serialize-strategy", havingValue = SerializeStrategy.KRYO)
    public Serializer kryoSerializer() {
        return new KryoSerializer();
    }

    @Bean
    @ConditionalOnProperty(prefix = "com.hx.broker", name = "serialize-strategy", havingValue = SerializeStrategy.PROTOBUF)
    public Serializer protobufSerializer() {
        return new ProtobufSerializer();
    }
}
