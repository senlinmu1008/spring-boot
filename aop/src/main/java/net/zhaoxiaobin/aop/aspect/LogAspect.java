/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.aop.aspect;

import cn.hutool.json.JSONUtil;
import net.zhaoxiaobin.aop.annotation.NeedAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author zhaoxb
 * @create 2019-11-12 21:28
 */
@Order(10)
@Aspect
@Component
@Slf4j
public class LogAspect {
    /*
    beanId方式定义切入点
     */
    @Pointcut("bean(aopController)")
    public void methods(){}

    /*
    目标方法执行之前执行
     */
    @Before("methods()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("=====Before=====");

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("URL:[{}]", request.getRequestURL().toString());
        log.info("HTTP_METHOD:[{}]", request.getMethod());
        log.info("IP:[{}]", request.getRemoteAddr());
        log.info("CLASS_METHOD:[{}]", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS:\r\n{}", JSONUtil.toJsonPrettyStr(joinPoint.getArgs()));
    }

    /*
    目标方法执行之后必定执行，无论是否报错
    目标方法同时需要@NeedAspect注解的修饰，并且这里（通知）的形参名要与上面注解中的一致
     */
    @After("methods() && @annotation(annot)")
    public void doAfter(NeedAspect annot) {
        log.info("=====After=====");
        log.debug("=====注解值[{}]=====", annot.value());
    }

    /*
    目标方法有返回值且正常返回后执行
    这里（切入点）的形参名要与上面注解中的一致
     */
    @AfterReturning(pointcut = "methods()",returning = "response")
    public void doAfterReturning(Object response) {
        log.info("======AfterReturning=====\r\n{}", JSONUtil.toJsonPrettyStr(response));
    }

    /*
    目标方法抛出异常后执行
    目标方法同时需要@NeedAspect注解的修饰，并且这里（通知）的形参名要与上面注解中的一致
    可以声明来获取目标方法抛出的异常
     */
    @AfterThrowing(pointcut = "methods() && @annotation(annot)", throwing = "ex")
    public void doAfterThrowing(NeedAspect annot, Throwable ex) {
        log.error("=====AfterThrowing====={}", ex.getMessage());
        log.debug("=====注解值[{}]=====", annot.value());
    }

}