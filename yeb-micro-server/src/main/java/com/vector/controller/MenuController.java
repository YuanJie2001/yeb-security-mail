package com.vector.controller;



import com.vector.service.IMenuService;
import com.vector.vo.RespVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {
    @Resource
    private IMenuService menuService;

    /**
     * 通过用户名获取菜单
     * @return
     */
    @GetMapping("/menu")
    public RespVO getMenusByAdminId(){
        return menuService.getMenusByAdminId();
    }
}
