package net.zhaoxiaobin.libai.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author zhaoxb
 * @date 2020/08/22 8:45 下午
 */
@Data
public class DetailEntity {
    /**
     * 学校名称
     */
    @JacksonXmlProperty(localName = "schoolName")
    @XStreamAlias("schoolName")
    private String schoolName;

    /**
     * 地区编号
     */
    @JacksonXmlProperty(localName = "district")
    @XStreamAlias("district")
    private String district;

    /**
     * 等级
     */
    @JacksonXmlProperty(localName = "level")
    @XStreamAlias("level")
    private String level;
}