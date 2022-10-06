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
@TableName("t_salary_adjust")
public class SalaryAdjust implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工ID
     */
    @TableField("eid")
    private Integer eid;

    /**
     * 调薪日期
     */
    @TableField("asDate")
    private LocalDate asDate;

    /**
     * 调前薪资
     */
    @TableField("beforeSalary")
    private Integer beforeSalary;

    /**
     * 调后薪资
     */
    @TableField("afterSalary")
    private Integer afterSalary;

    /**
     * 调薪原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}
