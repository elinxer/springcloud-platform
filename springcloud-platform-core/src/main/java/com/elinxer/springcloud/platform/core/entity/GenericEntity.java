package com.elinxer.springcloud.platform.core.entity;

import java.io.Serializable;

/**
 * 统一带有主键实体
 */
public interface GenericEntity<PK extends Serializable> extends Entity {
    PK getId();

    void setId(PK id);
}
