package com.vector.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vector.pojo.Admin;
import com.vector.pojo.LoginEntity;
import com.vector.service.IAdminService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 自定义实现UserDetail认证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private IAdminService adminService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Admin> wrapper =
                new QueryWrapper<Admin>()
                        .eq("username", username);
        Admin admin = adminService.getOne(wrapper);
        if(Optional.ofNullable(admin).isPresent()){
            // 权限加载
            return new LoginEntity(admin,adminService.getRoles(admin.getId()));
        }
        throw new UsernameNotFoundException("用户不存在!");
    }
}