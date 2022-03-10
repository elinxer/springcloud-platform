package com.elinxer.springcloud.platform.core.validation.constant;


import com.elinxer.springcloud.platform.core.bean.IErrorCode;

/**
 * 检查器自动装配类
 */
public enum ValidationErrorEnum implements IErrorCode {

    /**
     * xxx
     */
    PLAT_VALID_0001("存在相同数据");

    private ValidationErrorEnum(String message) {
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMessage(Object... params) {
        return null;
    }
}
