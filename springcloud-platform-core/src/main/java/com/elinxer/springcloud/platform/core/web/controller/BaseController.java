package com.elinxer.springcloud.platform.core.web.controller;

import com.elinxer.springcloud.platform.core.exception.ValidationException;
import com.elinxer.springcloud.platform.core.validation.constant.ValidationErrorEnum;
import com.elinxer.springcloud.platform.core.validation.validate.SimpleValidateResults;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 控制器基类
 */
public class BaseController {

    /**
     * Validator.
     */
    protected Validator validator;

    @Autowired(required = false)
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> T tryValidate(T bean, Class... group) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean, group);
        if (!violations.isEmpty()) {
            SimpleValidateResults results = new SimpleValidateResults();
            for (ConstraintViolation<T> violation : violations) {
                results.addResult(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ValidationException(ValidationErrorEnum.PLAT_VALID_0001, results);
        }
        return bean;
    }

}
