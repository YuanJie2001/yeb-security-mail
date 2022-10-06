package com.vector.service;

import com.vector.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.pojo.Menu;
import com.vector.pojo.Role;
import com.vector.vo.AdminLoginVO;
import com.vector.vo.RespVO;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
public interface IAdminService extends IService<Admin> {

    RespVO login(AdminLoginVO adminLoginVO, HttpServletRequest request);

    RespVO logout();

    RespVO getAdminInfo(Principal principal);

    List<Role> getRoles(Integer adminId);
}
