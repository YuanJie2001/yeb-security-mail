package com.vector.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

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
@TableName("t_appraise")
public class Appraise implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工id
     */
    @TableField("eid")
    private Integer eid;

    /**
     * 考评日期
     */
    @TableField("appDate")
    private LocalDate appDate;

    /**
     * 考评结果
     */
    @TableField("appResult")
    private String appResult;

    /**
     * 考评内容
     */
    @TableField("appContent")
    private String appContent;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}
