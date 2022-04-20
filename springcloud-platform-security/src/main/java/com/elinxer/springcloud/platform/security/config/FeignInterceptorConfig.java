package com.elinxer.springcloud.platform.security.config;


import com.elinxer.springcloud.platform.core.constant.TraceConstant;
import com.elinxer.springcloud.platform.security.constant.UaaConstant;
import com.elinxer.springcloud.platform.security.utils.TokenUtils;
import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * feign拦截
 *
 * @author zhengqh
 * @version 3.0
 */
@Configuration
public class FeignInterceptorConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        RequestInterceptor requestInterceptor = template -> {
            //传递token
            //使用feign client访问别的微服务时，将accessToken header
            //config.anyRequest().permitAll() 非强制校验token
            if (TokenUtils.getToken() != null && !"".equals(TokenUtils.getToken())) {
                template.header(UaaConstant.TOKEN_HEADER, TokenUtils.getToken());
            }
            //传递traceId
            String traceId = StringUtils.hasText(MDC.get(TraceConstant.LOG_TRACE_ID)) ? MDC.get(TraceConstant.LOG_TRACE_ID) : MDC.get(TraceConstant.LOG_B3_TRACEID);
            if (StringUtils.hasText(traceId)) {
                template.header(TraceConstant.HTTP_HEADER_TRACE_ID, traceId);
            }
        };

        return requestInterceptor;
    }
}
