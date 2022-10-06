package com.vector.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * url
     */
    @TableField("url")
    private String url;

    /**
     * path
     */
    @TableField("path")
    private String path;

    /**
     * 组件
     */
    @TableField("component")
    private String component;

    /**
     * 菜单名
     */
    @TableField("name")
    private String name;

    /**
     * 图标
     */
    @TableField("iconCls")
    private String iconCls;

    /**
     * 是否保持激活
     */
    @TableField("keepAlive")
    private Boolean keepAlive;

    /**
     * 是否要求权限
     */
    @TableField("requireAuth")
    private Boolean requireAuth;

    /**
     * 父id
     */
    @TableField("parentId")
    private Integer parentId;

    /**
     * 是否启用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<Menu> children;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private List<Role> roles;
}
