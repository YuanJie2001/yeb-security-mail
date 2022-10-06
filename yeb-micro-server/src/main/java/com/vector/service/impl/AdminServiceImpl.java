package com.vector.service.impl;

import com.vector.mapper.RoleMapper;
import com.vector.pojo.Admin;
import com.vector.mapper.AdminMapper;
import com.vector.pojo.LoginEntity;
import com.vector.pojo.Role;
import com.vector.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    private final RedissonClient redissonClient;

    public AdminServiceImpl(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }


    @Resource
    private RoleMapper roleMapper;

    @Override
    public RespVO  login() {
        return RespVO.ok("登录成功!");
    }

    @Override
    public RespVO logout() {
        // 获取SecurityContextHolder中的用户id
        // UserDetailsService返回对象
        LoginEntity loginEntity = (LoginEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = loginEntity.getUsername();
        redissonClient.getBucket("token:" + username).delete();
        return RespVO.ok("退出成功!");
    }

    @Override
    public RespVO getAdminInfo(Principal principal) {
        if (Optional.ofNullable(principal).isPresent()) {
            return RespVO.ok().put(principal.getName());
        }
        return RespVO.error("身份验证已过期,请重新登录!");
    }

    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

}
