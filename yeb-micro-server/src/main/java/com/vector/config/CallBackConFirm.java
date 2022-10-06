package com.vector.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.vector.pojo.MailLog;
import com.vector.service.IMailLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author WangJiaHui
 * @description: 交换机回调确认(以下回调功能会被备份交换机功能覆盖,如果两者同时开启,备份交换机优先级高)
 * @ClassName CallBackConFirm
 * @date 2022/9/22 13:47
 */
@Component
@Slf4j
public class CallBackConFirm implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{
    @Resource
    private IMailLogService mailLogService;

    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * ConfirmCallback接口注入RabbitTemplate
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 消息确认成功回调函数,确认消息是否到达broker
     * correlationData: 消息唯一标识
     * ack: 确认结果
     * cause: 不可投递原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msgId =
                Optional.ofNullable(correlationData).isPresent()
                        ? correlationData.getId()
                        : " ";
        if (ack) {
            log.info("{}=======>消息发送成功!", msgId);
            mailLogService.update(
                    new UpdateWrapper<MailLog>()
                            .set("status", 1)
                            .eq("msgId", msgId));
        } else {
            log.error("{}======>消息发送失败,原因是{}", msgId, cause);
        }
    }

    // 当消息传递过程中不可达目的地时将消息返回给生产者
    // 只有不可达目的地时候 才进行回退
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        // 可以在这里做保存内存重发
        log.error("消息:{},被交换机:{}回退给生产者,回退的原因:{},路由key:{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }

}
