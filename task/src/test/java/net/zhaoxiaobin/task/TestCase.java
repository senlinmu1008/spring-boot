/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.task;

import cn.hutool.core.net.NetUtil;
import cn.hutool.system.SystemUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.quartz.CronExpression;

import java.net.Inet4Address;
import java.util.Date;

/**
 *
 * @author zhaoxb
 * @create 2020-04-15 15:18
 */
public class TestCase {
    @Test
    @SneakyThrows
    public void testCron() {
        System.out.println(NetUtil.getLocalhostStr());
        System.out.println(SystemUtil.getHostInfo().getAddress());
        System.out.println(Inet4Address.getLocalHost().getHostAddress());
    }

    @Test
    @SneakyThrows
    public void testResCron() {
        CronExpression cronExpression = new CronExpression("* */5 7-23 * * ?");
        boolean resCron = cronExpression.isSatisfiedBy(new Date());
        System.out.println(resCron);
    }
}