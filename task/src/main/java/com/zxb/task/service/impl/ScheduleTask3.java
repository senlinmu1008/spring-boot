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
 *
 * @author zhaoxb
 * @create 2020-04-14 23:12
 */
@Service
@Slf4j
@Async("scheduleTaskExecutor")
public class ScheduleTask3 implements ScheduleService {
    @Override
    public void execute() {
        String now = DateUtil.now();
        log.info("ScheduleTask3:[{}]", now);
    }
}