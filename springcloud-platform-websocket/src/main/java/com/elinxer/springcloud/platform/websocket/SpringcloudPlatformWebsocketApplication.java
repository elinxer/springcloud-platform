package com.elinxer.springcloud.platform.websocket;

import com.elinxer.springcloud.platform.websocket.bootstrap.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author elinx
 */
@SpringBootApplication
public class SpringcloudPlatformWebsocketApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(SpringcloudPlatformWebsocketApplication.class, args);
        SpringContextUtil.setApplicationContext(app);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
