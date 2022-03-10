package com.elinxer.springcloud.platform.core.dict;

import java.util.List;

/**
 * 通用枚举的接口
 */
public interface ItemDefine extends EnumDict<String> {

    String getText();

    String getValue();

    String getComments();

    int getOrdinal();

    @Override
    default int ordinal() {
        return getOrdinal();
    }

    List<ItemDefine> getChildren();

}
