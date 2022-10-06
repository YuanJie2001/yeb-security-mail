package com.vector.security.handler;

import com.vector.pojo.Menu;
import com.vector.pojo.Role;
import com.vector.service.IMenuService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author WangJiaHui
 * @description: 根据请求url分析所需角色(权限控制)
 * @ClassName PermissionFilter
 * @date 2022/9/12 22:43
 *
 * 该Filter中的getAttributes方法获取到的是访问每条路径所需要的角色，
 * 并将其存储在类型为ConfigAttribute的Collection中作为返回值；
 * 接下来就要判断当前用户角色是否与访问路径所需角色相匹配，
 * 实现AccessDecisionManager接口的MyAccessDecisionManager类就是完成这件事的。
 */
@Component
public class PermissionFilter implements FilterInvocationSecurityMetadataSource {

    @Resource
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 获取请求url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> menusWithRole = menuService.getMenusWithRole();
        for (Menu menu : menusWithRole) {
            // 判断请求url与菜单角色是否匹配
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        // 没匹配到,创建默认角色
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
