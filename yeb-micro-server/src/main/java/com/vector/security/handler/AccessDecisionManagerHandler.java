package com.vector.security.handler;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author WangJiaHui
 * @description: 权限控制 具体判断用户角色
 * @ClassName AccessDecisionManager
 * @date 2022/9/13 7:56
 */
@Component
public class AccessDecisionManagerHandler implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            // 当前url所需角色
            String attribute = configAttribute.getAttribute();
            // 判断角色是否登录,登录赋予默认角色.此角色在PermissionFilter中设置
            if("ROLE_LOGIN".equals(attribute)){
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登陆,请登录!");
                }else {
                    return;
                }
            }

            // 判断用户角色是否为url所需角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if(authority.getAuthority().equals(attribute)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足,请联系管理员!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
