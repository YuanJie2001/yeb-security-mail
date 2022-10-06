package com.vector.controller;

import com.vector.annotation.SystemLog;
import com.vector.service.IAdminService;
import com.vector.vo.AdminLoginVO;
import com.vector.vo.RespVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author WangJiaHui
 * @description: 登录接口
 * @ClassName LoginController
 * @date 2022/9/7 19:04
 */
@RestController
public class LoginController {
    @Resource
    private IAdminService adminService;
    /**
     *  登录之后返回token
      */
    @PostMapping("/login")
    @SystemLog(businessName = "登录业务")
    public RespVO login(@Valid @RequestBody AdminLoginVO adminLoginVO,
                        HttpServletRequest request){
         return adminService.login(adminLoginVO,request);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public RespVO logout(){
        return adminService.logout();
    }
}
