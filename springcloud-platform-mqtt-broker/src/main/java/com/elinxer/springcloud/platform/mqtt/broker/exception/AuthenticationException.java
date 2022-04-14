

package com.elinxer.springcloud.platform.mqtt.broker.exception;

/**
 * 客户端认证异常
 *
 */
public class AuthenticationException extends GlobalException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}