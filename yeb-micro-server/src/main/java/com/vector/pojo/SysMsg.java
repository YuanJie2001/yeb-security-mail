package com.vector.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
@TableName("t_sys_msg")
public class SysMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息id
     */
    @TableField("mid")
    private Integer mid;

    /**
     * 0表示群发消息
     */
    @TableField("type")
    private Integer type;

    /**
     * 这条消息是给谁的
     */
    @TableField("adminid")
    private Integer adminid;

    /**
     * 0 未读 1 已读
     */
    @TableField("state")
    private Integer state;


}
