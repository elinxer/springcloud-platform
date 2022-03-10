package com.elinxer.springcloud.platform.core.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 验证结果
 */
public interface ValidateResults extends Serializable {

    boolean isSuccess();

    List<Result> getResults();

    interface Result extends Serializable {
        String getField();

        String getMessage();
    }

}
