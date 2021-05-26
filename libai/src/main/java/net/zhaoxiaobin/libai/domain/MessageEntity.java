package net.zhaoxiaobin.libai.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author zhaoxb
 * @date 2020/08/22 8:33 下午
 */
@Data
@JacksonXmlRootElement(localName = "msg")
@XStreamAlias("msg")
public class MessageEntity {
    /**
     * 报文头
     */
    @JacksonXmlProperty(localName = "head")
    @XStreamAlias("head")
    private HeadEntity headEntity;

    /**
     * 报文体
     */
    @JacksonXmlProperty(localName = "body")
    @XStreamAlias("body")
    private BodyEntity bodyEntity;
}