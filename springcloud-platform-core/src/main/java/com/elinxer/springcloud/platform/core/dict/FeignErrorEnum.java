package com.elinxer.springcloud.platform.core.dict;

import com.elinxer.springcloud.platform.core.bean.ErrorCode;
import com.elinxer.springcloud.platform.core.bean.IErrorCode;

/**
 * 系统异常常量
 */
public enum FeignErrorEnum implements IErrorCode {

    /**
     * xxxxx
     */
    PLAT_COMMON_0001("feign调用回传结构异常");

    private final ErrorCode errorCode;

    private FeignErrorEnum(String message) {
        errorCode = new ErrorCode(this.name(), message);
    }

    @Override
    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage(Object... params) {
        return errorCode.getMessage(params);
    }
}
