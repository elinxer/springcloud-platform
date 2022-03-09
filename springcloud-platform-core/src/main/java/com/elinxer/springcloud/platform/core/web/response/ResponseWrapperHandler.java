package com.elinxer.springcloud.platform.core.web.response;


import com.alibaba.fastjson.JSON;
import com.elinxer.springcloud.platform.core.web.annotation.ResponseWrapper;
import com.elinxer.springcloud.platform.core.web.intercepter.ResponseWrapperInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;


/**
 * @author elinx
 */
@Slf4j
@RestControllerAdvice
public class ResponseWrapperHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        ResponseWrapper responseWrapper = (ResponseWrapper) request.getAttribute(ResponseWrapperInterceptor.RESPONSE_RESULT_WRAPPER_TRUE);
        return responseWrapper != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        // 兼容流式编程
//        if (body instanceof Mono) {
//            return ((Mono<?>) body)
//                    .map(Response::ok)
//                    .switchIfEmpty(Mono.just(Response.ok()));
//        }
//        // 兼容流式编程
//        if (body instanceof Flux) {
//            return ((Flux<?>) body)
//                    .collectList()
//                    .map(Response::ok)
//                    .switchIfEmpty(Mono.just(Response.ok()));
//        }

        // <dependency>
        //            <groupId>io.projectreactor</groupId>
        //            <artifactId>reactor-core</artifactId>
        //            <version>3.3.0.RELEASE</version>
        //        </dependency>

        if (body instanceof String) {
            return JSON.toJSONString(Response.ok(body));
        }
        return Response.ok(body);
    }

}
