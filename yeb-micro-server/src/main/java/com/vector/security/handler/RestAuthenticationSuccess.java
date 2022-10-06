package com.vector.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vector.pojo.LoginEntity;
import com.vector.utils.JwtUtil;
import com.vector.vo.RespVO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJiaHui
 * @description: 认证成功处理器
 * @ClassName RestAuthenticationSuccess
 * @date 2022/10/4 9:46
 */
@Component
public class RestAuthenticationSuccess implements AuthenticationSuccessHandler {
    @Resource
    private RedissonClient redissonClient;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = response.getWriter();

        LoginEntity loginEntity = (LoginEntity) authentication.getPrincipal();

        if (!loginEntity.isEnabled()) {
            throw new RuntimeException("账号被禁用,请联系管理员");
        }
        // 生成token 即加密username
        String jwt = JwtUtil.createJWT(loginEntity.getAdmin().getUsername());
        Map<String, String> result = new HashMap<>();
        result.put("tokenHead", JwtUtil.TOKEN_BEARER);
        result.put("token", jwt);

        // 键token:username   值loginEntity对象
        RBucket<Object> token = redissonClient.getBucket("token:" + loginEntity.getUsername());
        token.set(loginEntity, 36, TimeUnit.MINUTES);

        RespVO respVO = RespVO.ok("登录成功!").put(result);
        writer.write(new ObjectMapper().writeValueAsString(respVO));
        writer.flush();
        writer.close();
    }
}
