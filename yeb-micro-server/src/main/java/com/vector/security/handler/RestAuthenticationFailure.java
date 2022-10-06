package com.vector.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vector.vo.RespVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WangJiaHui
 * @description: 认证失败处理器
 * @ClassName RestAuthenticationFailure
 * @date 2022/10/4 9:47
 */
@Component
public class RestAuthenticationFailure implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        PrintWriter writer = response.getWriter();
        RespVO respVO = RespVO.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "登陆失败!"+ exception.getMessage());
        writer.write(new ObjectMapper().writeValueAsString(respVO));
        writer.flush();
        writer.close();
    }
}
