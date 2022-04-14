package com.elinxer.springcloud.platform.mqtt.broker.exception;

/**
 * 校验参数异常
 */
public class CheckParamsException extends RuntimeException{

    public CheckParamsException() {
        super();
    }

    public CheckParamsException(String message) {
        super(message);
    }

    public CheckParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckParamsException(Throwable cause) {
        super(cause);
    }

    protected CheckParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}