/**
 * Copyright (C), 2015-2020
 */
package com.zxb.task;

import lombok.SneakyThrows;
import org.junit.Test;
import org.quartz.CronExpression;

import java.util.Date;

/**
 *
 * @author zhaoxb
 * @create 2020-04-15 15:18
 */
public class TestCase {
    @Test
    public void testCron() {
        String cron = "*/5 * * * * ?";
        System.out.println(CronExpression.isValidExpression(cron));
    }

    @Test
    @SneakyThrows
    public void testResCron() {
        CronExpression cronExpression = new CronExpression("* */5 7-23 * * ?");
        boolean resCron = cronExpression.isSatisfiedBy(new Date());
        System.out.println(resCron);
    }
}