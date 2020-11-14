/**
 * Copyright (C), 2015-2019
 */
package com.zxb.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxb
 * @create 2019-10-26 19:51
 */
@RestController
@Slf4j
public class WarnController {

    @PostMapping("/warn")
    public String warn() {
        MDC.put("seq", "1287561336062091404");
        log.debug("debug:{}", "=====debug=====");
        log.error("error:{}", "=====error=====");
        log.warn("warn:{}", "=====warn=====");
        log.info("info:{}", "=====info=====");
        MDC.clear();
        return "success";
    }
}