package com.elinxer.springcloud.platform.gateway.config;

import com.elinxer.springcloud.platform.gateway.handle.ResAccessDeniedHandler;
import com.elinxer.springcloud.platform.gateway.handle.ResAuthenticationEntryPoint;
import com.elinxer.springcloud.platform.gateway.handle.ResAuthenticationFailureHandler;
import com.elinxer.springcloud.platform.gateway.handle.ResAuthenticationSuccessHandler;
import com.elinxer.springcloud.platform.gateway.token.AuthorizeConfigManager;
import com.elinxer.springcloud.platform.gateway.token.TokenAuthenticationConverter;
import com.elinxer.springcloud.platform.gateway.token.TokenAuthenticationManager;
import com.elinxer.springcloud.platform.security.config.PermitUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.WebFilter;

/**
 * 资源服务器UAAClientAutoConfig
 *
 * @author zhengqh
 * @version 3.0.0
 */

@Configuration
@SuppressWarnings("all")
@EnableConfigurationProperties(PermitUrlProperties.class)
@EnableWebFluxSecurity
public class UAAClientAutoConfig {
    @Autowired
    private PermitUrlProperties permitUrlProperties;

    @Autowired
    private TokenStore tokenStore;
 
    @Autowired
    private AuthorizeConfigManager authorizeConfigManager ;

    @Value("${security.oauth2.enable:true}")
    private boolean tokenEnable;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        //认证处理器
        ReactiveAuthenticationManager tokenAuthenticationManager = new TokenAuthenticationManager(tokenStore);
        ResAuthenticationEntryPoint resAuthenticationEntryPoint = new ResAuthenticationEntryPoint();

        ResAccessDeniedHandler resAccessDeniedHandler = new ResAccessDeniedHandler() ;

        //构建Bearer Token
        //请求参数强制加上 Authorization BEARER token
        http.addFilterAt((WebFilter) (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if(request.getQueryParams().getFirst("access_token")!=null) {
                exchange.getRequest().mutate().headers(httpHeaders ->
                        httpHeaders.add(
                            "Authorization",
                            OAuth2AccessToken.BEARER_TYPE+" "+request.getQueryParams().getFirst("access_token"))
                );
            }
            return chain.filter(exchange);
        }, SecurityWebFiltersOrder.FIRST);
        
        //身份认证

        if (tokenEnable) {
            AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(tokenAuthenticationManager);
            authenticationWebFilter.setAuthenticationFailureHandler(new ResAuthenticationFailureHandler()); //登陆验证失败
            authenticationWebFilter.setAuthenticationSuccessHandler(new ResAuthenticationSuccessHandler()); //认证成功
            //token转换器
            TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter();
            tokenAuthenticationConverter.setAllowUriQueryParameter(true);
            authenticationWebFilter.setServerAuthenticationConverter(tokenAuthenticationConverter);
            http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        }

        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange = http.authorizeExchange();
         
        authorizeExchange.matchers(EndpointRequest.toAnyEndpoint()).permitAll(); //无需进行权限过滤的请求路径
        authorizeExchange.pathMatchers(permitUrlProperties.getIgnored()).permitAll() ;//无需进行权限过滤的请求路径
       if (tokenEnable) {
           authorizeExchange
                   .pathMatchers(HttpMethod.OPTIONS).permitAll()    //option 请求默认放行
                   //.anyExchange().access(authorizeConfigManager)    // 应用api权限控制
                    .anyExchange().authenticated()                    //token 有效性控制
                   .and()
                   .exceptionHandling()
                   .accessDeniedHandler(resAccessDeniedHandler)
                   .authenticationEntryPoint(resAuthenticationEntryPoint)
                   .and()
                   .headers()
                   .frameOptions()
                   .disable()
                   .and()
                   .httpBasic().disable()
                   .csrf().disable();
        } else {
           authorizeExchange
                   .pathMatchers(HttpMethod.OPTIONS).permitAll()    //option 请求默认放行
                   //.anyExchange().access(authorizeConfigManager)    // 应用api权限控制
                   .anyExchange().permitAll()                    //token 有效性控制
                   .and()
                   .exceptionHandling()
                   .accessDeniedHandler(resAccessDeniedHandler)
                   .authenticationEntryPoint(resAuthenticationEntryPoint)
                   .and()
                   .headers()
                   .frameOptions()
                   .disable()
                   .and()
                   .httpBasic().disable()
                   .csrf().disable();
       }
       return http.build();
    }
}
