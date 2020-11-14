/**
 * Copyright (C), 2015-2019
 */
package com.zxb.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面注解
 * @author zhaoxb
 * @create 2019-11-12 21:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedAspect {
    String value() default "";
}
