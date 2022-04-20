package com.elinxer.springcloud.platform.security.interceptor;

import com.elinxer.springcloud.platform.core.constant.TraceConstant;
import com.elinxer.springcloud.platform.security.constant.UaaConstant;
import com.elinxer.springcloud.platform.security.utils.TokenUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 传递token与traceId
 *
 * @author zhengqh
 * @date 2021-08-10
 */
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest httpRequest = attributes.getRequest();
        String header = httpRequest.getHeader(UaaConstant.AUTHORIZATION);

        String token = !StringUtils.hasText(TokenUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE + " ")) ?
                httpRequest.getParameter(OAuth2AccessToken.ACCESS_TOKEN) :
                TokenUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE + " ");
        token = !StringUtils.hasText(httpRequest.getHeader(UaaConstant.TOKEN_HEADER)) ? token : httpRequest.getHeader(UaaConstant.TOKEN_HEADER);
        //传递token
        HttpHeaders headers = request.getHeaders();
        headers.add(UaaConstant.TOKEN_HEADER, token);
        //传递traceId
        String traceId = StringUtils.hasText(MDC.get(TraceConstant.LOG_TRACE_ID)) ? MDC.get(TraceConstant.LOG_TRACE_ID) : MDC.get(TraceConstant.LOG_B3_TRACEID);
        if (StringUtils.hasText(traceId)) {
            headers.add(TraceConstant.HTTP_HEADER_TRACE_ID, traceId);
        }
        // 保证请求继续执行
        return execution.execute(request, body);
    }
}
