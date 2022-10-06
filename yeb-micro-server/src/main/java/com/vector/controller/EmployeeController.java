package com.vector.controller;


import com.vector.service.IEmployeeService;
import com.vector.vo.RespVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private IEmployeeService employeeService;
    @GetMapping("/send-mail")
    public RespVO send(){
       return employeeService.send();
    }
}
