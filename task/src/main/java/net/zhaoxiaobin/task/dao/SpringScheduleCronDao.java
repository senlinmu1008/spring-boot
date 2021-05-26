package net.zhaoxiaobin.task.dao;

import net.zhaoxiaobin.task.domain.SpringScheduleCron;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpringScheduleCronDao {
    List<SpringScheduleCron> findByAppId(String appId);
    SpringScheduleCron findByBeanName(@Param("appId") String appId, @Param("beanName") String beanName);
    void updateByBeanName(@Param("newCron") String newCron, @Param("appId") String appId, @Param("beanName") String beanName);
    void changeEnable(@Param("enable") Byte enable, @Param("appId") String appId, @Param("beanName") String beanName);
}