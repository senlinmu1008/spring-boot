/**
 * Copyright (C), 2015-2019
 */
package com.zxb.swagger.web;

import cn.hutool.json.JSONUtil;
import com.thoughtworks.xstream.XStream;
import com.zxb.swagger.domain.request.Req_Entity;
import com.zxb.swagger.domain.response.Resp_Detail;
import com.zxb.swagger.domain.response.Resp_Entity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author zhaoxb
 * @create 2019-11-15 21:25
 */
@Slf4j
@RestController
@Api(tags = {"swagger报文接口测试"})
@RequestMapping("/swagger")
public class SwaggerController {

    @ApiOperation(value = "json-mapping", notes = "测试请求报文字段映射转换-json")
    @RequestMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp_Entity json(@RequestBody @Validated Req_Entity request) {
        return response(request);
    }

    @ApiOperation(value = "xml-mapping", notes = "测试请求报文字段映射转换-xml")
    @RequestMapping(value = "/xml", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public Resp_Entity xml(@RequestBody @Validated Req_Entity request) {
        return response(request);
    }

    private Resp_Entity response(Req_Entity request) {
        log.info("请求报文:\r\n{}", JSONUtil.toJsonPrettyStr(request));
        // 转XML
        XStream xStream = new XStream();
        xStream.processAnnotations(Req_Entity.class);
        String xml = xStream.toXML(request);
        log.info("请求对象转xml:\r\n{}", xml);

        List<Resp_Detail> detailList = IntStream.range(1, 6).mapToObj(i -> new Resp_Detail().setOrgId(i + "").setOrgName("机构名称" + i)).collect(Collectors.toList());
        Resp_Entity respEntity = new Resp_Entity()
                .setReturnCode("000000")
                .setReturnMsg("交易成功")
                .setDetails(detailList);
        log.info("返回报文:\r\n{}", JSONUtil.toJsonPrettyStr(respEntity));
        return respEntity;
    }
}