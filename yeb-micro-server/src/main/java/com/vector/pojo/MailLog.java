package com.vector.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_mail_log")
public class MailLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    @TableId("msgId")
    private String msgId;

    /**
     * 接收员工id
     */
    @TableField("eid")
    private Integer eid;

    /**
     * 状态（0:消息投递中 1:投递成功 2:投递失败）
     */
    @TableField("status")
    private Integer status;

    /**
     * 路由键
     */
    @TableField("routeKey")
    private String routeKey;

    /**
     * 交换机
     */
    @TableField("exchange")
    private String exchange;

    /**
     * 重试次数
     */
    @TableField("count")
    private Integer count;

    /**
     * 重试时间
     */
    @TableField("tryTime")
    private LocalDateTime tryTime;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;


}
