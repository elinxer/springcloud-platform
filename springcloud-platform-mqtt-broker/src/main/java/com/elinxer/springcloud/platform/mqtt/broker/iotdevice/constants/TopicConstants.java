package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.constants;

public class TopicConstants {

    //登入登出
    public static final String TBOX_LOGINOUT_TOPIC="VehicleLoginout";

    //数据上报
    public static final String TBOX_DATA_REPORTED_TOPIC="VehicleDataUploader";

    //数据上报补发
    public static final String TBOX_DATA_REPORTED_REPARIR_TOPIC="VehicleDataUploaderRepair";

    //数据告警
    public static final String TBOX_ALARM_INFO_TOPIC="VehicleAlarm";

    //远程控制
    public static final String TBOX_VECHICLE_CONTROL_TOPIC="VehicleControl";

    public static final String TBOX_VECHICLE_CONTROL_GROUP="VehicleControlGroup";

    //远程控制
    public static final String TBOX_VECHICLE_CONTROL_ACK_TOPIC="VehicleControlAck";



    //推送指令到设备主题
    public static final String PUSH_VEHICLE_CONTROL_TOPIC="vehicle/%s/tbox";

    //远程控制
    public static final String IOT_DEVICE_CONTROL_TOPIC="iot_device_control";

    public static final String IOT_DEVICE_CONTROL_GROUP="iot_device_control_group";

}