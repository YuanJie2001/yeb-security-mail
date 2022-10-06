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

//        Employee employee = new Employee();
//        employee.setName("狗东西");
//        employee.setGender("男");
//        employee.setAddress("平顶山学院向量");
//        employee.setWedlock("单身狗");
//        employee.setNativePlace("河南");
//        employee.setEmail("3232691860@qq.com");
//        employee.setPhone("120");
//        employee.setEngageForm("在职");
//        employee.setTiptopDegree("本科");
//        employee.setSpecialty("软件工程");
//        employee.setSchool("平顶山学院");
//        employee.setWorkState("摸鱼");
//        employee.setWorkID("123456");
//        list.add(employee);
//        Employee employee1 = new Employee();
//        employee1.setName("张锐哲");
//        employee1.setGender("男");
//        employee1.setAddress("平顶山学院向量");
//        employee1.setWedlock("单身狗");
//        employee1.setNativePlace("河南");
//        employee1.setEmail("905913818@qq.com");
//        employee1.setPhone("xxx");
//        employee1.setEngageForm("在职");
//        employee1.setTiptopDegree("本科");
//        employee1.setSpecialty("软件工程");
//        employee1.setSchool("平顶山学院");
//        employee1.setWorkState("摸鱼");
//        employee1.setWorkID("123456");
//        list.add(employee1);
//        Employee employee2 = new Employee();
//        employee2.setName("李昀尘");
//        employee2.setGender("男");
//        employee2.setAddress("平顶山学院向量");
//        employee2.setWedlock("单身狗");
//        employee2.setNativePlace("河南");
//        employee2.setEmail("229250919@qq.com");
//        employee2.setPhone("xxxx");
//        employee2.setEngageForm("在职");
//        employee2.setTiptopDegree("本科");
//        employee2.setSpecialty("软件工程");
//        employee2.setSchool("平顶山学院");
//        employee2.setWorkState("摸鱼");
//        employee2.setWorkID("123456");
//        list.add(employee2);
//        Employee employee3 = new Employee();
//        employee3.setName("郭润菲");
//        employee3.setGender("女");
//        employee3.setAddress("平顶山学院向量");
//        employee3.setWedlock("单身狗");
//        employee3.setNativePlace("河南");
//        employee3.setEmail("1254661490@qq.com");
//        employee3.setPhone("xxxx");
//        employee3.setEngageForm("在职");
//        employee3.setTiptopDegree("本科");
//        employee3.setSpecialty("软件工程");
//        employee3.setSchool("平顶山学院");
//        employee3.setWorkState("摸鱼");
//        employee3.setWorkID("123456");
//        list.add(employee3);
//        Employee employee4 = new Employee();
//        employee4.setName("王添乐");
//        employee4.setGender("男");
//        employee4.setAddress("平顶山学院向量");
//        employee4.setWedlock("单身狗");
//        employee4.setNativePlace("河南");
//        employee4.setEmail("blue-meteor@qq.com");
//        employee4.setPhone("xxxx");
//        employee4.setEngageForm("在职");
//        employee4.setTiptopDegree("本科");
//        employee4.setSpecialty("软件工程");
//        employee4.setSchool("平顶山学院");
//        employee4.setWorkState("摸鱼");
//        employee4.setWorkID("123456");
//        list.add(employee4);
//        Employee employee5 = new Employee();
//        employee5.setName("徐怀昂");
//        employee5.setGender("男");
//        employee5.setAddress("平顶山学院向量");
//        employee5.setWedlock("单身狗");
//        employee5.setNativePlace("河南");
//        employee5.setEmail("2533694604@qq.com");
//        employee5.setPhone("xxxx");
//        employee5.setEngageForm("在职");
//        employee5.setTiptopDegree("本科");
//        employee5.setSpecialty("软件工程");
//        employee5.setSchool("平顶山学院");
//        employee5.setWorkState("摸鱼");
//        employee5.setWorkID("123456");
//        list.add(employee5);
//        Employee employee6 = new Employee();
//        employee6.setName("姚永琪");
//        employee6.setGender("男");
//        employee6.setAddress("平顶山学院向量");
//        employee6.setWedlock("单身狗");
//        employee6.setNativePlace("河南");
//        employee6.setEmail("2903249079@qq.com");
//        employee6.setPhone("xxxx");
//        employee6.setEngageForm("在职");
//        employee6.setTiptopDegree("本科");
//        employee6.setSpecialty("软件工程");
//        employee6.setSchool("平顶山学院");
//        employee6.setWorkState("摸鱼");
//        employee6.setWorkID("123456");
//        list.add(employee6);
//        Employee employee7 = new Employee();
//        employee7.setName("刘潇洒");
//        employee7.setGender("男");
//        employee7.setAddress("平顶山学院向量");
//        employee7.setWedlock("单身狗");
//        employee7.setNativePlace("河南");
//        employee7.setEmail("2565016460@qq.com");
//        employee7.setPhone("xxxx");
//        employee7.setEngageForm("在职");
//        employee7.setTiptopDegree("本科");
//        employee7.setSpecialty("软件工程");
//        employee7.setSchool("平顶山学院");
//        employee7.setWorkState("摸鱼");
//        employee7.setWorkID("123456");
//        list.add(employee7);
//        Employee employee8 = new Employee();
//        employee8.setName("林俊杰");
//        employee8.setGender("男");
//        employee8.setAddress("平顶山学院向量");
//        employee8.setWedlock("单身狗");
//        employee8.setNativePlace("河南");
//        employee8.setEmail("2020317877@qq.com");
//        employee8.setPhone("xxxx");
//        employee8.setEngageForm("在职");
//        employee8.setTiptopDegree("本科");
//        employee8.setSpecialty("软件工程");
//        employee8.setSchool("平顶山学院");
//        employee8.setWorkState("摸鱼");
//        employee8.setWorkID("123456");
//        list.add(employee8);
        Employee employee9 = new Employee();
        employee9.setName("莫家梁");
        employee9.setGender("男");
        employee9.setAddress("平顶山学院向量");
        employee9.setWedlock("不是单身狗");
        employee9.setNativePlace("河南");
        employee9.setEmail("2502513332@qq.com");
        employee9.setPhone("xxxx");
        employee9.setEngageForm("在职");
        employee9.setTiptopDegree("本科");
        employee9.setSpecialty("软件工程");
        employee9.setSchool("平顶山学院");
        employee9.setWorkState("摸鱼");
        employee9.setWorkID("123456");
        list.add(employee9);
        Employee emp1 = new Employee();
        emp1.setName("张冬冬");
        emp1.setGender("男");
        emp1.setAddress("郑州");
        emp1.setWedlock("单身狗");
        emp1.setNativePlace("河南");
        emp1.setEmail("2501514609@qq.com");
        emp1.setPhone("xxxx");
        emp1.setEngageForm("在职");
        emp1.setTiptopDegree("本科");
        emp1.setSpecialty("软件工程");
        emp1.setSchool("xxxx");
        emp1.setWorkState("摸鱼");
        emp1.setWorkID("123456");
        list.add(emp1);
        Employee emp2 = new Employee();
        emp2.setName("闫龙飞");
        emp2.setGender("男");
        emp2.setAddress("郑州");
        emp2.setWedlock("单身狗");
        emp2.setNativePlace("河南");
        emp2.setEmail("3244844648@qq.com");
        emp2.setPhone("xxxx");
        emp2.setEngageForm("在职");
        emp2.setTiptopDegree("本科");
        emp2.setSpecialty("软件工程");
        emp2.setSchool("xxxx");
        emp2.setWorkState("摸鱼");
        emp2.setWorkID("123456");
        list.add(emp2);

        Employee emp3 = new Employee();
        emp3.setName("张志远");
        emp3.setGender("男");
        emp3.setAddress("郑州");
        emp3.setWedlock("单身狗");
        emp3.setNativePlace("河南");
        emp3.setEmail("2686757508@qq.com");
        emp3.setPhone("xxxx");
        emp3.setEngageForm("在职");
        emp3.setTiptopDegree("本科");
        emp3.setSpecialty("软件工程");
        emp3.setSchool("xxxx");
        emp3.setWorkState("摸鱼");
        emp3.setWorkID("123456");
        list.add(emp3);

        Employee emp4 = new Employee();
        emp4.setName("张志远");
        emp4.setGender("男");
        emp4.setAddress("郑州");
        emp4.setWedlock("单身狗");
        emp4.setNativePlace("河南");
        emp4.setEmail("2686757508@qq.com");
        emp4.setPhone("xxxx");
        emp4.setEngageForm("在职");
        emp4.setTiptopDegree("本科");
        emp4.setSpecialty("软件工程");
        emp4.setSchool("xxxx");
        emp4.setWorkState("摸鱼");
        emp4.setWorkID("123456");
        list.add(emp4);

        Employee emp5 = new Employee();
        emp5.setName("吴嵩涛");
        emp5.setGender("男");
        emp5.setAddress("郑州");
        emp5.setWedlock("单身狗");
        emp5.setNativePlace("河南");
        emp5.setEmail("3209840064@qq.com");
        emp5.setPhone("xxxx");
        emp5.setEngageForm("在职");
        emp5.setTiptopDegree("本科");
        emp5.setSpecialty("软件工程");
        emp5.setSchool("xxxx");
        emp5.setWorkState("摸鱼");
        emp5.setWorkID("123456");
        list.add(emp5);

        Employee emp6 = new Employee();
        emp6.setName("张海强");
        emp6.setGender("男");
        emp6.setAddress("郑州");
        emp6.setWedlock("单身狗");
        emp6.setNativePlace("河南");
        emp6.setEmail("2243984372@qq.com");
        emp6.setPhone("xxxx");
        emp6.setEngageForm("在职");
        emp6.setTiptopDegree("本科");
        emp6.setSpecialty("软件工程");
        emp6.setSchool("xxxx");
        emp6.setWorkState("摸鱼");
        emp6.setWorkID("123456");
        list.add(emp6);
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
