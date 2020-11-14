/**
 * Copyright (C), 2015-2020
 */
package com.zxb.task.config;

import com.zxb.task.dao.SpringScheduleCronDao;
import com.zxb.task.domain.SpringScheduleCron;
import com.zxb.task.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 定时任务开关
        if (!scheduleSwitch) {
            return;
        }
        // 查询所有定时任务
        List<SpringScheduleCron> cronList = dao.findAll();
        for (SpringScheduleCron originSpringScheduleCron : cronList) {
            ScheduleService scheduleBean = context.getBean(originSpringScheduleCron.getBeanName(), ScheduleService.class);
            // 注册定时任务
            taskRegistrar.addTriggerTask(scheduleBean, triggerContext -> {
                        /*
                        每次定时任务执行时，都会执行以下代码
                        1.从表中查询当前定时任务最新的配置参数，更新下一次的执行周期
                        2.执行具体的定时任务业务逻辑
                         */
                        SpringScheduleCron springScheduleCron = dao.selectByBeanName(originSpringScheduleCron.getBeanName());
                        String cronExpression = springScheduleCron.getCronExpression();
                        log.debug("=====更新[{}]最新cron表达式[{}]=====", originSpringScheduleCron.getBeanName(), cronExpression);
                        return new CronTrigger(cronExpression).nextExecutionTime(triggerContext);
                    }
            );
        }
    }
}