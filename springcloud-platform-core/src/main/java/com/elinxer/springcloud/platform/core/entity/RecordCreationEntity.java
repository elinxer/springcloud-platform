package com.elinxer.springcloud.platform.core.entity;

import java.util.Date;

/**
 * 带有createTime，CreateUserId字段的接口
 */
public interface RecordCreationEntity extends Entity {

    public Date getCreateTime();

    public void setCreateTime(Date createTime);

    public Long getCreateUserId();

    public void setCreateUserId(Long createUserId);

    default void setCreateTimeNow() {
        setCreateTime(new Date());
    }

}
