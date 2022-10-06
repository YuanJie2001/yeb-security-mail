package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author WangJiaHui
 * @description: 项目启动类
 * @ClassName YebApplication
 * @date 2022/9/7 10:15
 */
@SpringBootApplication
@MapperScan("com.vector.mapper")
@EnableScheduling
public class YebApplication {
    public static void main(String[] args) {
        SpringApplication.run(YebApplication.class,args);
    }
}
