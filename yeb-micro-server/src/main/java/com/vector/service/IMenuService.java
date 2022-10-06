package com.vector.service;

import com.vector.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.vo.RespVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @return
     */
    RespVO getMenusByAdminId();

    /**
     * 根据角色获取菜单列表
     */
    List<Menu> getMenusWithRole();
}
