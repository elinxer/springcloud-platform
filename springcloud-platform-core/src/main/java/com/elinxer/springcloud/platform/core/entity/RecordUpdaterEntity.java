package com.elinxer.springcloud.platform.core.entity;

import java.util.Date;

/**
 * 带有UpdateTime，UpdateUserId字段的接口
 */
public interface RecordUpdaterEntity extends Entity {

    public Date getUpdateTime();

    public void setUpdateTime(Date updateTime);

    public Long getUpdateUserId();

    public void setUpdateUserId(Long updateUserId);

    default void setUpdateTimeNow() {
        setUpdateTime(new Date());
    }

}
