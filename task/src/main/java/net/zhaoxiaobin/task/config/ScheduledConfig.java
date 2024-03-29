/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.task.config;

import cn.hutool.core.net.NetUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.zhaoxiaobin.task.dao.SpringScheduleCronDao;
import net.zhaoxiaobin.task.domain.SpringScheduleCron;
import net.zhaoxiaobin.task.service.ScheduleService;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;

/**
 * 动态定时任务配置类
 *
 * @author zhaoxb
 * @create 2020-04-13 22:54
 */
@Configuration
@Slf4j
public class ScheduledConfig implements SchedulingConfigurer {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private SpringScheduleCronDao dao;

    @Value("${scheduleSwitch:false}")
    private boolean scheduleSwitch;

    @Value("${spring.application.name:appId}")
    private String appId;

    @Value("${server.port}")
    private String port;

    @Override
    @SneakyThrows
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 定时任务开关
        if (!scheduleSwitch) {
            return;
        }
        // 查询所有定时任务
        List<SpringScheduleCron> cronList = dao.findByAppId(appId);
        for (SpringScheduleCron originSpringScheduleCron : cronList) {
            ScheduleService scheduleBean = context.getBean(originSpringScheduleCron.getBeanName(), ScheduleService.class);
            // 注册定时任务
            /*
            每次定时任务执行时，都会按顺序执行以下代码
            1.执行scheduleBean的run函数（在ScheduleService接口实现的Runnable方法），可以异步执行
            2.从表中查询当前定时任务最新的配置参数，更新下一次的执行周期
             */
            taskRegistrar.addTriggerTask(scheduleBean, triggerContext -> {
                        try {
                            SpringScheduleCron springScheduleCron = dao.findByBeanName(appId, originSpringScheduleCron.getBeanName());
                            String originalCronExpression = originSpringScheduleCron.getCronExpression();
                            String currentCronExpression = springScheduleCron.getCronExpression();
                            if (!originalCronExpression.equals(currentCronExpression) && CronExpression.isValidExpression(currentCronExpression)) {
                                log.info("=====更新[{}]最新cron表达式[{}]=====", originSpringScheduleCron.getBeanName(), currentCronExpression);
                                originSpringScheduleCron.setCronExpression(currentCronExpression);
                            }
                        } catch (Exception e) {
                            log.error("=====更新cron表达式异常=====", e);
                        }
                        return new CronTrigger(originSpringScheduleCron.getCronExpression()).nextExecutionTime(triggerContext);
                    }
            );
        }
        // 定时任务管理界面
        String contextPath = "http://".concat(NetUtil.getLocalhostStr()).concat(":").concat(port);
        log.info("定时任务管理页面：{}", contextPath.concat("/scheduleManagement/taskList"));

//        // 设置调度定时任务的线程池大小，防止因为调度缓慢导致别的定时任务不能及时启动（不会落但会延迟！！！）
//        // 最好通过new ThreadPoolExecutor方式创建而不是Executors方式
//        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(2));
    }
}