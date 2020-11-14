/**
 * Copyright (C), 2015-2020
 */
package com.zxb.task.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务配置类
 *
 * @author zhaoxb
 * @create 2020-04-12 21:58
 */
//@Component
@Slf4j
public class ScheduleTask {
    /**
     * cron表达式
     * 每3秒执行一次
     */
    @Scheduled(cron = "*/3 * * * * ?")
    public void run1() {
        log.info("======cron======");
    }

    /**
     * 启动后10秒开始执行，固定5秒周期执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 5000)
    public void run2() {
        log.info("======fixedRate======");
    }

    /**
     * 启动后10秒开始执行，距离上次执行结束之后20秒再开始执行下一次
     */
    @Scheduled(initialDelay = 10000, fixedDelay = 20000)
    public void run3() {
        log.info("======fixedDelay======");
    }
}