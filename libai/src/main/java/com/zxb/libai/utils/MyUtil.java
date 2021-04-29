/**
 * Copyright (C), 2015-2019
 */
package com.zxb.libai.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @author zhaoxb
 * @create 2019-09-30 20:48
 */
@Slf4j
public class MyUtil {
    /**
     * 取ip地址最后一段作为workerId
     */
    private static Snowflake snowflake = IdUtil.getSnowflake(Integer.parseInt(SystemUtil.getHostInfo().getAddress().split("\\.")[3]), 1L);

    /**
     * 生成19位流水号
     *
     * @return java.lang.String
     * @author zhaoxb
     * @date 2019-10-04 17:01
     */
    public static String genSeq() {
        return snowflake.nextIdStr();
    }

    /**
     * 请求参数验证不通过返回
     *
     * @return java.lang.String
     * @author zhaoxb
     * @date 2019-11-16 21:01
     */
    public static String argNotValid(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
            sb.append(error.getField() + "->" + error.getDefaultMessage());
        }
        return sb.toString();
    }

}