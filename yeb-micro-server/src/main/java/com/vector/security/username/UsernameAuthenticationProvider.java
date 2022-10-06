package com.vector.security.username;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author WangJiaHui
 * @description: 邮箱认证实现
 * @ClassName EmailAuthenticationProvider
 * @date 2022/10/4 8:21
 */
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService usernameDetailsServiceImpl;

    private final RedissonClient redissonClient;

    public UsernameAuthenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl, RedissonClient redissonClient) {
        this.usernameDetailsServiceImpl = userDetailsServiceImpl;
        this.redissonClient = redissonClient;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        Object principal = authentication.getPrincipal();// 获取凭证也就是用户的账号
        String username = "";
        if (principal instanceof String) {
            username = (String) principal;
        }
        String inputCode = (String) authentication.getCredentials(); // 获取输入的密码
        // 1. 检验Redis用户名的密码
        String redisCode = (String) redissonClient.getBucket(username).get();
        if (StringUtils.isEmpty(redisCode)) {
            throw new BadCredentialsException("登录已过期!,请重新登录!");
        }
        if (!inputCode.equals(redisCode)) {
            throw new BadCredentialsException("输入的密码不正确，请重新输入");
        }
        // 2. 根据用户名查询查询用户信息
        UserDetails userDetails = usernameDetailsServiceImpl.loadUserByUsername(username);
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("用户不存在，请注册");
        }
        // 3. 重新创建已认证对象,
        UsernamePasswordAuthenticationToken authenticationResult =
                new UsernamePasswordAuthenticationToken(principal,inputCode, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
