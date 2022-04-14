package com.elinxer.springcloud.platform.mqtt.broker.constants;

import java.util.HashMap;
import java.util.Map;

public class CommandTopic {

    //农机订阅主题 TOBX
    public static final String FARM_MACHINERY_TOPIC = "farmMachineryTopic";

    //无人机订阅主题
    public static final String UAV_TOPIC = "uavTopic";

    //无人船订阅主题
    public static final String UNMANNED_SHIP_TOPIC = "unmannedShipTopic";

    static final Map<Integer, String> TOPIC_MAP = new HashMap<>();

    static {
        TOPIC_MAP.put(0X01, FARM_MACHINERY_TOPIC);
        TOPIC_MAP.put(0X02, UAV_TOPIC);
        TOPIC_MAP.put(0X03, UNMANNED_SHIP_TOPIC);
    }
}