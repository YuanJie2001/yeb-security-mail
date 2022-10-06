package com.vector.service.impl;

import com.vector.config.RabbitmqConfig;
import com.vector.pojo.Employee;
import com.vector.mapper.EmployeeMapper;
import com.vector.pojo.MailLog;
import com.vector.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.service.IMailLogService;
import com.vector.vo.RespVO;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private IMailLogService mailLogService;
    public static ArrayList<Employee> list = new ArrayList<>();
    @Override
    public RespVO send() {
        String[] names = new String[]{"狗东西","张锐哲","李昀尘","郭润菲","王添乐",
                "徐怀昂","姚永琪","刘潇洒","林俊杰","莫老越要补了","张冬冬","闫龙飞","张志远",
                "吴嵩涛","张海强","高毅","李嘉昊"
        };
        String[] emails = new String[]{"3232691860@qq.com","905913818@qq.com",
                "229250919@qq.com","1254661490@qq.com","blue-meteor@qq.com","2533694604@qq.com",
                "2903249079@qq.com","2565016460@qq.com","2020317877@qq.com","2502513332@qq.com",
                "2501514609@qq.com","3244844648@qq.com","2686757508@qq.com","3209840064@qq.com",
                "2243984372@qq.com","h1371834358@qq.com","2049395838@qq.com"
        };
        for (int i=0;i< names.length;i++){
            Employee employee = new Employee();
            employee.setName(names[i]);
            employee.setGender("test");
            employee.setAddress("test");
            employee.setWedlock("test");
            employee.setNativePlace("test");
            employee.setEmail(emails[i]);
            employee.setPhone("test");
            employee.setEngageForm("test");
            employee.setTiptopDegree("test");
            employee.setSpecialty("test");
            employee.setSchool("test");
            employee.setWorkState("摸鱼");
            employee.setWorkID("test");
            list.add(employee);
        }
        List<MailLog> collect = list.stream().map(emp -> {
            // 消息落库
            String msgId = UUID.randomUUID().toString();
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setEid(emp.getId());
            mailLog.setStatus(0);
            mailLog.setRouteKey(RabbitmqConfig.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(RabbitmqConfig.MAIL_EXCHANGE_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(RabbitmqConfig.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            // 发送消息
            rabbitTemplate.convertAndSend(RabbitmqConfig.MAIL_EXCHANGE_NAME,
                    RabbitmqConfig.MAIL_ROUTING_KEY_NAME,
                    emp,
                    new CorrelationData(msgId));
            return mailLog;
        }).collect(Collectors.toList());
        mailLogService.saveBatch(collect);
        return RespVO.ok();
    }
}
