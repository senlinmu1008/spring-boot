package net.zhaoxiaobin.httpsaction.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxb
 * @date 2023-04-04 上午10:53
 */
@RestController
@Slf4j
public class HttpsController {

    @PostMapping(value = "/hello")
    public String hello(@RequestBody String requestBody) {
        log.info("请求报文:{}", requestBody);
        return "hello " + requestBody;
    }
}