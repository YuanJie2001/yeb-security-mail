package com.vector.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangJiaHui
 * @description: rabbit配置类
 * @ClassName RabbitmqConfig
 * @date 2022/9/22 8:32
 */
@Configuration
@Slf4j
public class RabbitmqConfig {
    // 消息超时时间
    public static final Integer MSG_TIMEOUT = 1;
    // 队列
    public static final  String MAIL_QUEUE_NAME = "mail.queue";
    // 交换机
    public static final  String MAIL_EXCHANGE_NAME = "mail.exchange";
    // 路由键
    public static final  String MAIL_ROUTING_KEY_NAME = "mail.routing.key";
    // 备份交换机
    public static final String BACKUP_EXCHANGE_NAME="backup_exhacnge";
    // 备份队列
    public static final String BACKUP_QUEUE_NAME="backup_queue";
    // 报警队列
    public static final String WARNING_QUEUE_NAME="warning_queue";

    // 声明交换机
    @Bean("mailExchange")
    public DirectExchange confirmExchange() {
        //
        return ExchangeBuilder.directExchange(MAIL_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange", MAIL_EXCHANGE_NAME).build();
    }

    // 声明队列
    @Bean("mailQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(MAIL_QUEUE_NAME).build();
    }

    // 声明备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    // 声明备份队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    // 声明报警队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }


    // 绑定
    @Bean
    public Binding queueBindingExchange(@Qualifier("mailQueue") Queue confirmQueue,
                                        @Qualifier("mailExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(MAIL_ROUTING_KEY_NAME);
    }

    // 绑定备份
    @Bean
    public Binding backupQueueBindingExchange(@Qualifier("backupQueue") Queue backupQueue,
                                              @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    // 绑定报警
    @Bean
    public Binding warningQueueBindingExchange(@Qualifier("warningQueue") Queue warningQueue,
                                               @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
