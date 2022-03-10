
package com.elinxer.springcloud.platform.core.validation.constraint;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


/**
 * 自定义邮箱验证注解实现类
 */
public class EmailValidator implements ConstraintValidator<Email, String> {
    private static final String REG_EX = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    private static final Pattern PATTERN = Pattern.compile(REG_EX);

    @Override
    public void initialize(Email parameters) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return PATTERN.matcher(value).matches();
    }
}
