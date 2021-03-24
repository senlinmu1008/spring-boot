package com.zxb.task.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * spring_schedule_cron
 *
 * @author zhaoxb
 */
@Data
public class SpringScheduleCron implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 定时任务beanName
     */
    private String beanName;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 状态 0-禁用 1-启用
     */
    private Byte enable;

    /**
     * 应用id
     */
    private String appId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpringScheduleCron that = (SpringScheduleCron) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(beanName, that.beanName) &&
                Objects.equals(cronExpression, that.cronExpression) &&
                Objects.equals(taskDesc, that.taskDesc) &&
                Objects.equals(enable, that.enable) &&
                Objects.equals(appId, that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beanName, cronExpression, taskDesc, enable, appId);
    }
}