package com.zxb.task.dao;

import com.zxb.task.domain.SpringScheduleCron;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpringScheduleCronDao {
    List<SpringScheduleCron> findAll();
    SpringScheduleCron selectByBeanName(String beanName);
    void updateByBeanName(@Param("newCron") String newCron, @Param("beanName") String beanName);
    void changeStatus(@Param("status") Byte status, @Param("beanName") String beanName);
}