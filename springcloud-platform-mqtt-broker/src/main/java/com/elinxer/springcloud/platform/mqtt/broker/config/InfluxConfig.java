/**
 * @description
 * @author caoxiaoguang
 * @create 2021-09-18 9:43
 **/
package com.elinxer.springcloud.platform.mqtt.broker.config;

import com.elinxer.springcloud.platform.mqtt.broker.influxdb.InfluxDBConnectionFactory;
import com.elinxer.springcloud.platform.mqtt.broker.influxdb.InfluxDBProperties;
import com.elinxer.springcloud.platform.mqtt.broker.influxdb.InfluxDBTemplate;
import com.elinxer.springcloud.platform.mqtt.broker.influxdb.converter.PointConverter;
import org.influxdb.dto.Point;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InfluxDBProperties.class)
@ConditionalOnProperty(prefix = "com.hx.influxdb", value = "enabled", havingValue = "true")
public class InfluxConfig {

    @Bean
    public InfluxDBConnectionFactory connectionFactory(final InfluxDBProperties properties) {
        return new InfluxDBConnectionFactory(properties);
    }

    @Bean
    public InfluxDBTemplate<Point> influxDBTemplate(final InfluxDBConnectionFactory connectionFactory) {
        return new InfluxDBTemplate<>(connectionFactory, new PointConverter());
    }
}