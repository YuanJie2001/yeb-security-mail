package com.vector.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vector.vo.RespVO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WangJiaHui
 * @description: 授权失败异常处理器
 * @ClassName RestfulAccessDeniedHandler
 * @date 2022/9/8 11:09
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        RespVO respVO = RespVO.error(403,"权限不足,请联系管理员");
        writer.write(new ObjectMapper().writeValueAsString(respVO));
        writer.flush();
        writer.close();
    }
}
