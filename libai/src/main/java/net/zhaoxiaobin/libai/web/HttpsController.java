package net.zhaoxiaobin.libai.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaoxb
 * @date 2020/12/22 11:39 下午
 */
@Slf4j
@RestController
public class HttpsController {
    @RequestMapping(value = "/p5-http-in/filetransfer/{sid}")
    public String testPathVariable(@PathVariable("sid") String sid) {
        return sid;
    }

    @RequestMapping(value = "/senddata")
    public String postBody(@RequestBody String requestBody, HttpServletRequest httpServletRequest) {
        String host = httpServletRequest.getHeader("Host");
        log.info("host:{}", host);
        log.info("请求体:{}", requestBody);
        return requestBody;
    }

    @RequestMapping(value = "/testDelay")
    public String testDelay(Integer time) throws InterruptedException {
        log.info("=======testDelay=======");
        if (time > 0 && time < 30) {
            Thread.sleep(time * 1000);
        }
        return "success";
    }
}