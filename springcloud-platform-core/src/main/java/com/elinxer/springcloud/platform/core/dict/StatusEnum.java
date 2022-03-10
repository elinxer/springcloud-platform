package com.elinxer.springcloud.platform.core.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举（整个框架统一）
 */
@AllArgsConstructor
@Getter
public enum StatusEnum implements EnumDict<Byte> {

    /**
     * 状态枚举
     */
    ENABLED((byte) 1, "正常"),
    DISABLED((byte) 0, "禁用");

    private Byte value;

    private String text;

}
