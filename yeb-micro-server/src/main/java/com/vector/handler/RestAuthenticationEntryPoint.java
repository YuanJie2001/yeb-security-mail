package com.vector.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vector.vo.RespVO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WangJiaHui
 * @description: 认证失败异常处理器
 * @ClassName RestAuthenticationEntryPoint
 * @date 2022/9/8 10:44
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        RespVO respVO = RespVO.error(401,"用户名或密码不正确!");
        writer.write(new ObjectMapper().writeValueAsString(respVO));
        writer.flush();
        writer.close();
    }
}
