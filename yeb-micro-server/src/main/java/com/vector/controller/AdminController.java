package com.vector.controller;


import com.vector.service.IAdminService;
import com.vector.vo.RespVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private IAdminService adminService;
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public RespVO getAdminInfo(Principal principal){
        return adminService.getAdminInfo(principal);
    }

    @GetMapping("/info-annotations")
    @Secured("ROLE_manager")
    public RespVO getInfo(Principal principal){
        return adminService.getAdminInfo(principal);
    }
}
