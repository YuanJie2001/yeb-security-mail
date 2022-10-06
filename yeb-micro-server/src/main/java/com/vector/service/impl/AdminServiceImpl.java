package com.vector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vector.mapper.RoleMapper;
import com.vector.pojo.Admin;
import com.vector.mapper.AdminMapper;
import com.vector.pojo.LoginEntity;
import com.vector.pojo.Menu;
import com.vector.pojo.Role;
import com.vector.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.utils.JwtUtil;
import com.vector.vo.AdminLoginVO;
import com.vector.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.convert.Bucket;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public RespVO login(AdminLoginVO adminLoginVO, HttpServletRequest request) {
        if (!adminLoginVO.getCode().equals(request.getSession().getAttribute("captcha"))) {
            return RespVO.error("验证码输入错误!");
        }
        // 登录 authenticationManager最终会调用UserDetailsService实现方法
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(adminLoginVO.getUsername(), adminLoginVO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // UserDetailsService返回对象
        LoginEntity loginEntity = (LoginEntity) authenticate.getPrincipal();
        /**
         * userDetails会在底层调用username和password字段进行匹配认证.可以在
         * AuthenticationEntryPoint中执行认证失败处理
         */
//        if(!passwordEncoder
//                .matches(adminLoginVO.getPassword(), loginEntity.getAdmin().getPassword())){
//            return RespVO.error("用户名或密码不正确");
//        }
        if (!loginEntity.isEnabled()) {
            return RespVO.error("账号被禁用,请联系管理员!");
        }
        // 生成token 即加密username
        String jwt = JwtUtil.createJWT(adminLoginVO.getUsername());
        Map<String, String> map = new HashMap<>();
        map.put("tokenHead", JwtUtil.TOKEN_BEARER);
        map.put("token", jwt);

        // 键token:username   值loginEntity对象
        RBucket<Object> token = redissonClient.getBucket("token:" + loginEntity.getUsername());
        token.set(loginEntity, 36, TimeUnit.MINUTES);
        return RespVO.ok("登录成功!").put(map);
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
