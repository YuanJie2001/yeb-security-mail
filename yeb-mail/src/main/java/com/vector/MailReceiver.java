package com.vector;

import com.rabbitmq.client.Channel;
import com.vector.config.RabbitmqConfig;
import com.vector.pojo.Employee;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJiaHui
 * @description: 接收邮箱
 * @ClassName MailReceiver
 * @date 2022/9/21 17:43
 */
@Component
@Slf4j
public class MailReceiver {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private MailProperties mailProperties;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private RedissonClient redissonClient;

    // 接收报警消息
    @RabbitListener(queues = RabbitmqConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(org.springframework.amqp.core.Message message){
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息: {}",msg);
    }

    @RabbitListener(queues = RabbitmqConfig.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        RMap<String, String> map = redissonClient.getMap("mail_log");

        try {
            if (map.containsValue(msgId)) {
                log.info("消息已经被消费=============> {}",msgId);
                /**
                 * 手动确认消息
                 * tag: 消息序号
                 * multiple: 是否确认多条
                 */
                channel.basicAck(tag,false);
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            // 发件人
            helper.setFrom(mailProperties.getUsername());
            // 收件人
            helper.setTo(employee.getEmail());
            // 邮件主题
            helper.setSubject("入职欢迎邮件");
            // 发送日期
            helper.setSentDate(new Date());
            Context context = new Context();
            // 邮件内容
            context.setVariable("name", employee.getName());
            context.setVariable("gender", employee.getGender());
            context.setVariable("address", employee.getAddress());
            context.setVariable("wedlock", employee.getWedlock());
            context.setVariable("nativePlace", employee.getNativePlace());
            context.setVariable("engageForm", employee.getEngageForm());
            context.setVariable("tiptopDegree", employee.getTiptopDegree());
            context.setVariable("specialty", employee.getSpecialty());
            context.setVariable("school", employee.getSchool());
            context.setVariable("workState", employee.getWorkState());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            javaMailSender.send(mimeMessage);
            log.info("邮件发送成功!");
            map.put(msgId, "ok");
            map.expire(300, TimeUnit.MICROSECONDS);
            // 手动确认单条异常
            channel.basicAck(tag, false);

            // 消息确认成功回调函数

        } catch (Exception e) {
            /**
             * tag: 消息序号
             * multiple: 是否确认多条
             * requeue: 是否退回到队列
             */
            try {
                channel.basicNack(tag,false,true);
            } catch (IOException ex) {
                log.error("邮件发送失败=========>{}", e.getMessage());
            }
            log.error("邮件发送失败=========>{}", e.getMessage());
        }

    }
}
