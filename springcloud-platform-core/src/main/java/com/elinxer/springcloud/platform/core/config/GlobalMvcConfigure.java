package com.elinxer.springcloud.platform.core.config;

import com.elinxer.springcloud.platform.core.intercepter.HeaderThreadLocalInterceptor;
import com.elinxer.springcloud.platform.core.intercepter.ResponseResultWrapperInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * MVC 全局配置
 *
 * @author elinx
 */
@Configuration
@AllArgsConstructor
public class GlobalMvcConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //处理ResponseResultWrapper注解的
        registry.addInterceptor(new ResponseResultWrapperInterceptor()).addPathPatterns("/**");

        registry.addInterceptor(new HeaderThreadLocalInterceptor())
                .addPathPatterns("/**")
                .order(-20);
    }
}
