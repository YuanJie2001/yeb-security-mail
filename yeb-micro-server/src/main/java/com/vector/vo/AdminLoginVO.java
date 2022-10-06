package com.vector.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author WangJiaHui
 * @description: 用户登录实体类
 * @ClassName AdminLoginVO
 * @date 2022/9/7 19:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminLoginVO {


    @NotNull(message = "登录必须提交用户名账号")
    private String username;
    @NotNull(message = "登录必须提交密码")
    private String password;
    @NotNull(message = "登录必须填写验证码")
    private String code;
}
