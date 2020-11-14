/**
 * Copyright (C), 2015-2019
 */
package com.zxb.actuator2x.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zhaoxb
 * @create 2019-11-27 22:14
 */
@RestController
@Slf4j
public class ActuatorController {

    @PostMapping("/query")
    public String query() {
        log.info("======query=====");
        return "success";
    }
}