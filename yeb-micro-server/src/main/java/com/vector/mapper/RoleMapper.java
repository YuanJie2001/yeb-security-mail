package com.vector.mapper;

import com.vector.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoles(@Param("adminId") Integer adminId);
}
