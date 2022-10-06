package com.vector.service;

import com.vector.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.pojo.Role;
import com.vector.vo.RespVO;

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

    RespVO login();

    RespVO logout();

    RespVO getAdminInfo(Principal principal);

    List<Role> getRoles(Integer adminId);
}
