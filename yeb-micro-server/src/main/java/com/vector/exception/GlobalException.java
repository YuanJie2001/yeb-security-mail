package com.vector.exception;

import com.vector.vo.RespVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author WangJiaHui
 * @description: 全局异常处理
 * @ClassName GlobalException
 * @date 2022/9/14 10:35
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public RespVO mysqlException(SQLException e){
        if(e instanceof SQLIntegrityConstraintViolationException){
            return RespVO.error("该数据有关联数据,操作失败!");
        }
        return RespVO.error("数据库异常,操作失败!");
    }
}
