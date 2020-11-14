/**
 * Copyright (C), 2015-2020
 */
package com.zxb.task;

import org.junit.Test;
import org.quartz.CronExpression;

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
}