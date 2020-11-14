/**
 * Copyright (C), 2015-2019
 */
package com.zxb.aop.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zxb.aop.annotation.NeedAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *
 * @author zhaoxb
 * @create 2019-11-12 20:36
 */
@RestController
@Slf4j
@RequestMapping("/aop")
public class AopController {

    @PostMapping("/add")
    @NeedAspect("add")
    public JSONObject add(@RequestBody JSONObject request) {
        log.info("=====请求报文=====\r\n{}", JSONUtil.toJsonPrettyStr(request));
        return success();
    }

    @PostMapping("/delete")
    @NeedAspect("delete")
    public JSONObject delete(Long id) {
        log.info("=====删除id:[{}]=====", id);
        return success();
    }

    @PostMapping("/update")
    @NeedAspect("update")
    public JSONObject update(@RequestBody JSONObject request) {
        log.info("=====请求报文=====\r\n{}", JSONUtil.toJsonPrettyStr(request));
        return success();
    }

    @PostMapping("/query")
    @NeedAspect("query")
    public JSONObject query(Long id) {
        log.info("=====查询id:[{}]=====", id);
        if(true) {
            throw new RuntimeException("error");
        }
        return success();
    }

    private JSONObject success() {
        JSONObject response = new JSONObject();
        response.put("returnCode", "000000");
        response.put("returnMsg", "成功");
        response.put("timestamp", DateUtil.formatDateTime(new Date()));
        response.put("data", "SpringBoot Aop");
        return response;
    }
}