package com.vector.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vector.config.RabbitmqConfig;
import com.vector.pojo.Employee;
import com.vector.pojo.MailLog;
import com.vector.service.IEmployeeService;
import com.vector.service.IMailLogService;
import com.vector.service.impl.EmployeeServiceImpl;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangJiaHui
 * @description: 邮件定时任务
 * @ClassName MailTask
 * @date 2022/9/22 9:05
 */
@Component
public class MailTask {
    @Resource
    private IMailLogService mailLogService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 邮件发送定时任务
     * 10s一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask() {
        if(CollectionUtils.isEmpty(EmployeeServiceImpl.list)){
            return;
        }
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", 0)
                .lt("tryTime", LocalDateTime.now()));
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        List<MailLog> collect = list.stream().map(mailLog -> {
            // 如果重试投递次数超过3,更新状态为投递失败,不再重试
            if (3 <= mailLog.getCount()) {
                mailLog.setStatus(2);

            }
            mailLog.setCount(mailLog.getCount()+1);
            mailLog.setUpdateTime(LocalDateTime.now().plusMinutes(RabbitmqConfig.MSG_TIMEOUT));
            Employee employee = EmployeeServiceImpl.list.get(0);
            rabbitTemplate.convertAndSend(RabbitmqConfig.MAIL_EXCHANGE_NAME,
                    RabbitmqConfig.MAIL_ROUTING_KEY_NAME,
                    employee,
                    new CorrelationData(mailLog.getMsgId()));
            return mailLog;
        }).collect(Collectors.toList());
        // 更新数据库信息
        mailLogService.saveOrUpdateBatch(collect);
    }
}
