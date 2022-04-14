/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elinxer.springcloud.platform.mqtt.broker.utils;

import com.elinxer.springcloud.platform.mqtt.broker.config.MqttxConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * 用于加载生成 {@link KeyManagerFactory} 及 {@link TrustManagerFactory}
 *
 */
@Component
@Slf4j
public class SslUtils {

    private SSLContext sslContext;

    public SslUtils(MqttxConfig mqttxConfig) {
        MqttxConfig.Ssl ssl = mqttxConfig.getSsl();
        if (!Boolean.TRUE.equals(ssl.getEnable())) {
            return;
        }
        InputStream serverStream = null;
        InputStream trustStream = null;
        try {
            ClassPathResource resource =new ClassPathResource(ssl.getKeyServerStoreLocation());
            serverStream =resource.getInputStream();
            KeyStore serverKeyStore = KeyStore.getInstance(ssl.getKeyStoreType());
            serverKeyStore.load(serverStream, ssl.getKeyServerPassword().toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(serverKeyStore, ssl.getKeyServerPassword().toCharArray());

            KeyStore serverTrustKeyStore = KeyStore.getInstance(ssl.getKeyStoreType());
            ClassPathResource trustResource =new ClassPathResource(ssl.getTrustKeyServerStoreLocation());
            trustStream =trustResource.getInputStream();

            serverTrustKeyStore.load(trustStream, ssl.getTrustKeyServerPassword().toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(serverTrustKeyStore);

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            this.sslContext = sslContext;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getSslContext-ex={}", e);
        } finally {
            if (serverStream != null) {
                try {
                    serverStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (trustStream != null) {
                try {
                    trustStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public SSLContext getSslContext() {
        return this.sslContext;
    }


}