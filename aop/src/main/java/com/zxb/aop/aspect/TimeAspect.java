/**
 * Copyright (C), 2015-2019
 */
package com.zxb.aop.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 * @author zhaoxb
 * @create 2019-11-12 22:57
 */
@Order(1)
@Aspect
@Component
@Slf4j
public class TimeAspect {
    /*
    execution表达式定义切入点
     */
    @Pointcut("execution(public * com.zxb.aop.web..*.*(..))")
    public void methods(){}

    /*
    环绕通知，可以获取到目标方法的入参和返回值
    通过切入点的proceed方法向后调用，先执行Before前置通知方法，接着调用目标方法
    如果有其它切面的Order比当前切面的Order更大，则执行其它切面的After、AfterReturning(AfterThrowing)方法，最后获取返回值
    环绕通知结束后再执行当前切面的After、AfterReturning(AfterThrowing)相关方法
     */
    @Around("methods()")
    public Object doAround(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();

        Object[] args = point.getArgs();
        log.info("=====环绕通知开始=====");

        Object result;
        try {
            result = point.proceed(args);
        } catch (Throwable e) {
            log.error("=====切面捕获异常=====", e);
            JSONObject response = new JSONObject();
            response.put("returnCode", "999999");
            response.put("returnMsg", "失败");
            response.put("timestamp", DateUtil.formatDateTime(new Date()));
            result = response;
        }

        log.info("环绕通知结束:\r\n{}", JSONUtil.toJsonPrettyStr(result));
        long end = System.currentTimeMillis();
        log.info("=====耗时:[{}]ms=====", end - start);
        return result;
    }
}