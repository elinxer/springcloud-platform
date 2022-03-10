package com.elinxer.springcloud.platform.core.validation.group;

import javax.validation.groups.Default;

/**
 * 使用此group,只在修改的时候才进行验证
 */
public interface UpdateGroup extends Default {
}
