package com.elinxer.springcloud.platform.core.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除标志的枚举（整个框架统一）
 */
@AllArgsConstructor
@Getter
public enum DeletedEnum implements EnumDict<Byte> {

    ENABLED((byte) 0, "正常"),
    DISABLED((byte) 1, "删除");

    private Byte value;

    private String text;
}
