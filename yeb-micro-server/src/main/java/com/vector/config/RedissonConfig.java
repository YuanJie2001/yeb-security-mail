package com.vector.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author WangJiaHui
 * @description: test
 * @ClassName RedissonConfig
 * @date 2022/3/5 10:11
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
public class RedissonConfig {
    private String host;
    private String password;
    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        //config.useClusterServers().addNodeAddress("127.0.0.1:6379").setPassword("123456");
        config.useSingleServer()
                .setAddress("redis://"+ this.getHost())
                .setPassword(this.getPassword());
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}