package com.elinxer.springcloud.platform.core.feign;

import lombok.Data;

/**
 * Feign回传结构
 */
@Data
public class FeignFailResult {

    private int status;
    private String msg;

}
