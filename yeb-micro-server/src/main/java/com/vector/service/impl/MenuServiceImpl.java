package com.vector.service.impl;

import com.vector.pojo.LoginEntity;
import com.vector.pojo.Menu;
import com.vector.mapper.MenuMapper;
import com.vector.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.vo.RespVO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
   @Resource
   private MenuMapper menuMapper;
   @Resource
   private RedissonClient redissonClient;
    /**
     * 通过用户名获取菜单
     * @return
     */
    @Override
    public RespVO getMenusByAdminId() {
        LoginEntity loginEntity = (LoginEntity) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        RBucket<List<Menu>> bucket = redissonClient.getBucket("menu_" + loginEntity.getAdmin().getId());
        List<Menu> menus = bucket.get();
        if(CollectionUtils.isEmpty(menus)){
            menus = menuMapper.getMenusByAdminId(loginEntity.getAdmin().getId());
            bucket.set(menus);
        }
        return RespVO.ok().put(menus);
    }

    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }
}
