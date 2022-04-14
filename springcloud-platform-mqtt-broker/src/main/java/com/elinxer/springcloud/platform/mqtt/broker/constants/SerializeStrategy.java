package com.elinxer.springcloud.platform.mqtt.broker.constants;

/**
 * 序列化策略
 */
public interface SerializeStrategy {

    String JSON = "json";

    String KRYO = "kryo";

    String PROTOBUF = "protobuf";
}
