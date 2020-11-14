package com.zxb.swagger.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *
 * @author zhaoxb
 * @date 2019-11-15 22:48
 * @return
 */
@Accessors(chain = true)
@Data
@XStreamAlias("BODY")
@JacksonXmlRootElement(localName = "BODY")
public class Resp_Entity {
    @ApiModelProperty(value = "响应码")
    @XStreamAlias("RETURN_CODE")
    @JsonProperty("RETURN_CODE")
    @JacksonXmlProperty(localName = "RETURN_CODE")
    private String returnCode;

    @ApiModelProperty(value = "响应信息")
    @XStreamAlias("RETURN_MSG")
    @JsonProperty("RETURN_MSG")
    @JacksonXmlProperty(localName = "RETURN_MSG")
    private String returnMsg;

    @ApiModelProperty(value = "响应详细信息")
    @XStreamAlias("DETAILS")
    @JsonProperty("DETAILS")
    @JacksonXmlProperty(localName = "DETAILS")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Resp_Detail> details;

}
