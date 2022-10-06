package com.vector.vo;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WangJiaHui
 * @description: 公共返回对象
 * @ClassName RespVOespVO
 * @date 2022/9/7 13:49
 */

public class RespVO extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    @Override
    public RespVO put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public RespVO put(Object value) {
        super.put("data", value);
        return this;
    }
    public Object  getData(){
        return this.get("data");
    }

    public RespVO() {
        put("code", 200);
        put("msg", "success");
    }

    public static RespVO error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
    }

    public static RespVO error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static RespVO error(int code, String msg) {
        RespVO r = new RespVO();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static RespVO ok(String msg) {
        RespVO r = new RespVO();
        r.put("msg", msg);
        return r;
    }

    public static RespVO ok(Map<String, Object> map) {
        RespVO r = new RespVO();
        r.putAll(map);
        return r;
    }

    public static RespVO ok() {
        return new RespVO();
    }


    public Integer getCode() {
        return (Integer) this.get("code");
    }

}
