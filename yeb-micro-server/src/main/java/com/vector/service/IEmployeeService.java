package com.vector.service;

import com.vector.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.vo.RespVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
public interface IEmployeeService extends IService<Employee> {

    RespVO send();
}
