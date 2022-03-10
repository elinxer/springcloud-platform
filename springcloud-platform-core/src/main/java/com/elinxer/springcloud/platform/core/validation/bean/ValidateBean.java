package com.elinxer.springcloud.platform.core.validation.bean;


import com.elinxer.springcloud.platform.core.bean.CopyBean;


public interface ValidateBean extends CopyBean {
    /**
     * 尝试验证此bean,如果验证未通过
     *
     * @param group 验证分组
     * @param <T>   当前对象类型
     * @return 当前对象
     */
    default <T extends ValidateBean> T tryValidate(Class... group) {
        BeanValidator.tryValidate(this, group);
        return (T) this;
    }
}
