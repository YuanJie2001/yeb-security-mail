package com.vector.controller;


import com.vector.pojo.Employee;
import com.vector.service.IEmployeeService;
import com.vector.vo.RespVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@RequestMapping("/mail-log")
public class MailLogController {
}
