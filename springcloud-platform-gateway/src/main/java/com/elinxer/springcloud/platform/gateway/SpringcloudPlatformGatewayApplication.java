package com.elinxer.springcloud.platform.gateway;

import com.elinxer.springcloud.platform.core.config.GlobalFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(defaultConfiguration = GlobalFeignConfig.class)
@SpringBootApplication
public class SpringcloudPlatformGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudPlatformGatewayApplication.class, args);
    }

}
