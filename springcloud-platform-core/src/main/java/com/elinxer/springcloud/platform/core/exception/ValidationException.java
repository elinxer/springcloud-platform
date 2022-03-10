package com.elinxer.springcloud.platform.core.exception;


import com.elinxer.springcloud.platform.core.bean.IErrorCode;
import com.elinxer.springcloud.platform.core.bean.ValidateResults;

import java.util.List;

/**
 * 有效性检查异常类
 *
 * @author elinx
 */
public class ValidationException extends FrameWorkException {

    private ValidateResults results;

    public ValidationException(IErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }

    public ValidationException(Throwable cause, IErrorCode errorCode, Object... params) {
        super(cause, errorCode, params);
    }

    public ValidationException(IErrorCode errorCode, ValidateResults results) {
        super(errorCode);
        this.results = results;
    }

    public List<ValidateResults.Result> getResults() {
        if (results == null) {
            return new java.util.ArrayList<>();
        }
        return results.getResults();
    }
}
