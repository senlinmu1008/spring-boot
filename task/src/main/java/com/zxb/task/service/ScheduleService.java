/**
 * Copyright (C), 2015-2020
 */
package com.zxb.task.service;


import com.zxb.task.dao.SpringScheduleCronDao;
import com.zxb.task.domain.SpringScheduleCron;
import com.zxb.task.utils.SpringUtils;
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
        execute();
    }
}
