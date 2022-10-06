package com.vector.mapper;

import com.vector.pojo.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenusByAdminId(@Param("id") Integer id);

    List<Menu> getMenusWithRole();
}
