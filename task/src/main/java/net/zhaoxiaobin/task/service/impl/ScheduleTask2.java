/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.task.service.impl;

import cn.hutool.core.date.DateUtil;
import net.zhaoxiaobin.task.service.ScheduleService;
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
@Async
public class ScheduleTask2 implements ScheduleService {
    @Override
    public void execute() {
        String now = DateUtil.now();
        log.info("ScheduleTask2:[{}]", now);
    }
}