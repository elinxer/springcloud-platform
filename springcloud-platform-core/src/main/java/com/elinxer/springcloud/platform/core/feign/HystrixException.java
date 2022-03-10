package com.elinxer.springcloud.platform.core.feign;


/**
 * feign client 避免熔断异常处理
 */
public class HystrixException extends HystrixBadRequestException {

    private static final long serialVersionUID = -2437160791033393978L;

    public HystrixException(String msg) {
        super(msg);
    }

    public HystrixException(Exception e) {
        this(e.getMessage());
    }
}
