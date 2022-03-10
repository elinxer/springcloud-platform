
package com.elinxer.springcloud.platform.core.validation.validate;

import java.io.Serializable;
import java.util.List;

/**
 * 验证结果
 *
 * @author zhengqh
 */
public interface ValidateResults extends Serializable {

    boolean isSuccess();

    List<Result> getResults();

    interface Result extends Serializable {
        String getField();

        String getMessage();
    }

}
