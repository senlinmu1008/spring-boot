/**
 * Copyright (C), 2015-2020
 */
package com.zxb.task.web;

import com.zxb.task.dao.SpringScheduleCronDao;
import com.zxb.task.service.ScheduleService;
import com.zxb.task.utils.SpringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 简易定时任务管理
 * @author zhaoxb
 * @create 2020-04-15 13:40
 */
@Controller
@RequestMapping("scheduleManagement")
public class ScheduleManagement {
    @Autowired
    private SpringScheduleCronDao dao;

    @Value("${spring.application.name:appId}")
    private String appId;

    /**
     * 页面列表展示
     */
    @GetMapping("taskList")
    public String taskList(HttpServletRequest request) {
        request.setAttribute("taskList", dao.findByAppId(appId));
        return "task-list";
    }

    /**
     * 修改cron表达式
     */
    @ResponseBody
    @RequestMapping("editTaskCron")
    public Integer editTaskCron(String newCron, String beanName) {
        if (!CronExpression.isValidExpression(newCron)) {
            return 2;
        }
        dao.updateByBeanName(newCron, appId, beanName);
        return 1;
    }

    /**
     * 执行定时任务
     */
    @ResponseBody
    @RequestMapping("runTaskCron")
    public Integer runTaskCron(String beanName) {
        ScheduleService scheduleService = SpringUtils.getBean(beanName, ScheduleService.class);
        scheduleService.execute();
        return 0;
    }

    /**
     * 启用/禁用定时任务
     */
    @ResponseBody
    @RequestMapping("changeEnableTaskCron")
    public Integer changeEnableTaskCron(Byte enable, String beanName) {
        dao.changeEnable(enable, appId, beanName);
        return 1;
    }
}