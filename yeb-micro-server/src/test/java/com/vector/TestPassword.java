package com.vector;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author WangJiaHui
 * @description: 生成密码
 * @ClassName TestPassword
 * @date 2022/9/10 11:23
 */
@SpringBootTest
@Slf4j
public class TestPassword {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Test
    void BCryptPasswordEncoderGenerate(){
        String encode = passwordEncoder.encode("123456");
        log.info("生成的加密密码为{}",encode);
    }
}
