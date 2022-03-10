package com.elinxer.springcloud.platform.core.validation.constraint;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义邮件验证注解
 *
 *
 * @author: zhengqh
 * @version:3.0.0
 */
@Documented
@Constraint(validatedBy = {com.elinxer.springcloud.platform.core.validation.constraint.EmailValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface Email {
    String message() default "请输入有效的邮箱";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
