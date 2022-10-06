package com.vector.security.username;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author WangJiaHui
 * @description: 自定义前后端分离认证
 * @ClassName LoginFilter
 * @date 2022/10/4 8:54
 */
public class UsernameFilter extends UsernamePasswordAuthenticationFilter {
    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    private final String CODE = "code";

    private final String CAPTCHA = "captcha";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 判断是否位为post请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 判断是否是json请求类型
        if (request.getContentType()
                .equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            // 从json中拿数据项
            Map<String, String> userInfo = null;
            try {
                userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String code = userInfo.get(CODE);
                if (StringUtils.isNotBlank(code)
                        && code.equalsIgnoreCase((String) request.getSession().getAttribute(CAPTCHA))) {
                    String username = userInfo.get(USERNAME);
                    String password = userInfo.get(PASSWORD);
                    UsernamePasswordAuthenticationToken authRequest = null;
                    if (StringUtils.isNotBlank(username)) {
                        authRequest =
                                new UsernamePasswordAuthenticationToken(username, password);
                    }
                    this.setDetails(request, authRequest);
                    // 放入spring security缓存
                    return this.getAuthenticationManager().authenticate(authRequest);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 不符合条件,调用原父类认证器
        return super.attemptAuthentication(request, response);
    }
}
