package com.elinxer.springcloud.platform.core.bean;

/**
 * 错误定义类
 */
public interface IErrorCode {

    /**
     * 错误编码
     *
     * @return 获取定义错误编码
     */
    String getCode();

    /**
     * 根据参数方式获取错误消息
     *
     * @param params 定义参数
     * @return 错误消息
     */
    String getMessage(Object... params);

}
