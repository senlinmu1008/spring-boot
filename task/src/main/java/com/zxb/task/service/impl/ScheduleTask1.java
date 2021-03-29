/**
 * Copyright (C), 2015-2020
 */
package com.zxb.task.service.impl;

import cn.hutool.core.date.DateUtil;
import com.zxb.task.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 1.定时任务类必须实现ScheduleService接口
 * 2.定时任务类beanName必须使用默认的规则即类名首字母小写
 * 3.定时任务需要以异步方式执行（添加@Async注解）
 *
 * @author zhaoxb
 * @create 2020-04-14 23:12
 */
@Service
@Slf4j
@Async
public class ScheduleTask1 implements ScheduleService {
    @Override
    public void execute() {
        String now = DateUtil.now();
        log.info("ScheduleTask1:[{}]", now);
    }
}