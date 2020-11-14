/**
 * Copyright (C), 2015-2018
 */
package com.zxb.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxb
 * @create 2018-10-22 23:26
 */
@RestController
@Slf4j
public class LogbackController {

    @PostMapping("/logback")
    public String logback() {
        MDC.put("seq", "1287561336062091404");
        log.debug("debug:{}", "=====debug=====");
        log.info("info:{}", "=====info=====");
        log.warn("warn:{}", "=====warn=====");
        log.error("error:{}", "=====error=====");
        MDC.clear();
        return "success";
    }
}