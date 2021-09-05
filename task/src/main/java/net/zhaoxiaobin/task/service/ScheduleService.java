/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.task.service;

import net.zhaoxiaobin.task.dao.SpringScheduleCronDao;
import net.zhaoxiaobin.task.domain.SpringScheduleCron;
import net.zhaoxiaobin.task.strategy.TaskSchedulingStrategy;
import net.zhaoxiaobin.task.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * 定时任务执行接口
 *
 * @author zhaoxb
 * @create 2020-04-14 22:12
 */
public interface ScheduleService extends Runnable {
    Logger log = LoggerFactory.getLogger(ScheduleService.class);

    void execute();

    @Override
    default void run() {
        // 获取当前执行的定时任务beanName
        String classPath = this.getClass().getName();
        String className = classPath.substring(classPath.lastIndexOf(".") + 1);
        String beanName = (className.charAt(0) + "").toLowerCase().concat(className.substring(1));
        // 查询是否启用
        Environment environment = SpringUtils.getBean(Environment.class);
        String appId = environment.getProperty("spring.application.name");
        SpringScheduleCronDao dao = SpringUtils.getBean(SpringScheduleCronDao.class);
        SpringScheduleCron springScheduleCron = dao.findByBeanName(appId, beanName);
        if (springScheduleCron.getEnable() != 1) {
            log.debug("=====[{}]不可用=====", beanName);
            return;
        }
//        execute(); // 直接执行

        // 根据调度策略决定任务是否执行
        TaskSchedulingStrategy taskSchedulingStrategy = SpringUtils.getBean("distributedSchedulingImpl", TaskSchedulingStrategy.class);
        if (taskSchedulingStrategy.currentCanExecute(springScheduleCron)) {
            execute();
        }
    }
}
