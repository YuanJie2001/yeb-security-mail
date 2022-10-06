package com.vector.security.handler;

import com.vector.pojo.Admin;
import com.vector.pojo.LoginEntity;
import com.vector.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * @ClassName JwtAuthenticationTokenFilter
 * @Description jwt token回传解密验证数据库信息.
 * @Author YuanJie
 * @Date 2022/8/25 20:43
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedissonClient redissonClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String authHeader = request.getHeader(JwtUtil.TOKEN_AUTHORIZATION);
        // token不存在
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(JwtUtil.TOKEN_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        // TOKEN_HEADER=TOKEN_HEAD TOKEN  截取token
        String authToken = authHeader.substring(JwtUtil.TOKEN_BEARER.length());
        // 解析token中的username
        String subject;
        try {
            subject = JwtUtil.getUserNameFromToken(authToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        // 从redis中获取用户信息 对应登录加密存储redis的键
        String redisKey = "token:" + subject;
        // redis的redisson整合框架
        RBucket<LoginEntity> bucket = redissonClient.getBucket(redisKey);
        LoginEntity loginEntity = bucket.get();
        if (!Optional.ofNullable(loginEntity).isPresent()) {
            throw new RuntimeException("验证已过期,请重新登录!");
        }
        // 存入SecurityContextHolder,让后续的过滤器链获取信息 三参构造标记已认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginEntity, null, loginEntity.getAuthorities());
        // 放入spring security缓存
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
