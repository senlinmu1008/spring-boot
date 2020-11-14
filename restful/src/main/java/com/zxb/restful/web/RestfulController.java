package com.zxb.restful.web;

import cn.hutool.json.JSONUtil;
import com.zxb.restful.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxb
 * @date 2020/10/05 9:46 上午
 */
@RestController
@Slf4j
public class RestfulController {
    @GetMapping(value = "/getUser1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User getUser1() {
        return new User(1L, "zhaoxb");
    }

    @GetMapping(value = "/getUser2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User getUser2(User user) {
        log.info("getUser2:{}", JSONUtil.toJsonPrettyStr(user));
        return user;
    }

    @PostMapping(value = "/postUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User postUser(User user) {
        log.info("postUser:{}", JSONUtil.toJsonPrettyStr(user));
        return user;
    }

    @PostMapping(value = "/postBody", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User postBody(@RequestBody User user) {
        log.info("postBody:{}", JSONUtil.toJsonPrettyStr(user));
        return user;
    }
}