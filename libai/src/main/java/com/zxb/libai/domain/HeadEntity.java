package com.zxb.libai.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author zhaoxb
 * @date 2020/08/22 8:33 下午
 */
@Data
public class HeadEntity {
    /**
     * 应用名称
     */
    @JacksonXmlProperty(localName = "app")
    @XStreamAlias("app")
    private String app;

    /**
     * 版本号
     */
    @JacksonXmlProperty(localName = "version")
    @XStreamAlias("version")
    private String version;

    /**
     * 日期，yyyyMMdd
     */
    @JacksonXmlProperty(localName = "date")
    @XStreamAlias("date")
    private String date;

    /**
     * 时间，HHmmss
     */
    @JacksonXmlProperty(localName = "time")
    @XStreamAlias("time")
    private String time;

    /**
     * 交易码
     */
    @JacksonXmlProperty(localName = "txnCode")
    @XStreamAlias("txnCode")
    private String txnCode;

    /**
     * 流水号
     */
    @JacksonXmlProperty(localName = "seqNo")
    @XStreamAlias("seqNo")
    private String seqNo;

    /**
     * 预留字段
     */
    @JacksonXmlProperty(localName = "reserve")
    @XStreamAlias("reserve")
    private String reserve;
}