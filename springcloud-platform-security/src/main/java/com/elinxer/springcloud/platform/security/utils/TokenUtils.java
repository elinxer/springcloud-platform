package com.elinxer.springcloud.platform.security.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.elinxer.springcloud.platform.security.constant.UaaConstant;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * TokenUtils工具类
 *
 * @author zhengqh
 * @version 3.0.0
 */
public class TokenUtils {

    public static String substringAfter(String str, String separator) {
        if (StringUtils.isBlank(str)) {
            return str;
        } else if (separator == null) {
            return "";
        } else {
            int pos = str.indexOf(separator);
            return pos == -1 ? "" : str.substring(pos + separator.length());
        }
    }

    public static String getToken() {
        String token = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String header = request.getHeader(UaaConstant.AUTHORIZATION);
            token = StringUtils.isBlank(TokenUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE + " ")) ?
                    request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) : TokenUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE + " ");
            token = StringUtils.isBlank(request.getHeader(UaaConstant.TOKEN_HEADER)) ? token : request.getHeader(UaaConstant.TOKEN_HEADER);
        } catch (IllegalStateException e) {
        }
        return token;
    }

    public static String getToken(Map<String, List<String>> parameterMap) {
        String token = "";
        try {
            List<String> tokenList = parameterMap.get(OAuth2AccessToken.ACCESS_TOKEN);
            token = tokenList.get(0);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static String extractToken(ServerHttpRequest request) {
        List<String> strings = request.getHeaders().get(UaaConstant.AUTHORIZATION);
        String authToken = "";
        if (strings != null && strings.size() >= 1 && strings.get(0).contains("Bearer")) {
            authToken = strings.get(0).substring("Bearer".length()).trim();
        }
        if (StringUtils.isBlank(authToken)) {
            strings = request.getQueryParams().get(UaaConstant.TOKEN_PARAM);
            if (strings != null && strings.size() >= 1) {
                authToken = strings.get(0);
            }
        }
        return authToken;
    }
}
