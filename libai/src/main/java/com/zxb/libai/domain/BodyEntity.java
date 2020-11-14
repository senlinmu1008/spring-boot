package com.zxb.libai.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoxb
 * @date 2020/08/22 8:38 下午
 */
@Data
public class BodyEntity {
    /**
     * 标签属性
     */
    @JacksonXmlProperty(isAttribute = true, localName = "attr")
    @XStreamAsAttribute
    private String attr;

    /**
     * 客户姓名
     */
    @JacksonXmlProperty(localName = "custName")
    @XStreamAlias("custName")
    private String custName;

    /**
     * 证件号码
     */
    @JacksonXmlProperty(localName = "idNo")
    @XStreamAlias("idNo")
    private String idNo;

    /**
     * 地址
     */
    @JacksonXmlProperty(localName = "address")
    @XStreamAlias("address")
    private String address;

    /**
     * 爱好
     */
    // 包装循环域的父标签名称，仅在 useWrapping = true时有效，false则不使用父标签包装循环域
    @JacksonXmlElementWrapper(localName = "hobbies")
    @JacksonXmlProperty(localName = "hobby")
    // 使用Xstream库处理xml时，循环域没有父级标签包装 @XStreamImplicit 注解用来指定循环域标签名称
    @XStreamImplicit(itemFieldName = "hobby")
    private List<String> hobbies;

    /**
     * 详细信息
     */
    @JacksonXmlElementWrapper(useWrapping = false) // 不使用父标签包装循环域
    @JacksonXmlProperty(localName = "detail")
    @XStreamImplicit(itemFieldName = "detail") // 循环域的标签
    private List<DetailEntity> details;
}