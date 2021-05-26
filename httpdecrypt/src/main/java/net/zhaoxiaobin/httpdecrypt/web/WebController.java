/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.httpdecrypt.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zhaoxb
 * @create 2019-10-03 11:35
 */
@RestController
@Slf4j
@RequestMapping("/decrypt")
public class WebController {

    @PostMapping("/test")
    public String test(@RequestBody String requestBody) {
        log.info("经过解密后的数据:{}", requestBody);
        return "success-交易成功";
    }
}