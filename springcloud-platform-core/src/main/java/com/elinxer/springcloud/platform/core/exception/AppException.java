package com.elinxer.springcloud.platform.core.exception;

import com.elinxer.springcloud.platform.core.bean.IErrorCode;

/**
 * 应用层异常
 */
public class AppException extends FrameWorkException {

    public AppException(IErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }

    public AppException(Throwable cause, IErrorCode errorCode, Object... params) {
        super(cause, errorCode, params);
    }

    public AppException(String errCode, Object... params) {
        super(getErrorCode(errCode), params);
    }

    public static IErrorCode getErrorCode(String code) {
        return null;  //todo
    }

}
