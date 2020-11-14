package com.zxb.swagger.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 
 * @author zhaoxb
 * @date 2019-11-15 22:51
 * @return 
 */
@Accessors(chain = true)
@Data
public class Req_Detail {

    @ApiModelProperty(value = "学校", example = "浙江大学", required = true)
    @NotBlank(message = "学校不能为空")
    @XStreamAlias("SCHOOL")
    @JsonProperty("SCHOOL")
    @JacksonXmlProperty(localName = "SCHOOL")
    @JacksonXmlCData
    @Length(max = 240, message = "学校不能超过240")
    private String school;

    @ApiModelProperty(value = "邮政编码", example = "310000", required = true)
    @NotBlank(message = "邮政编码不能为空")
    @XStreamAlias("ZIP_CODE")
    @JsonProperty("ZIP_CODE")
    @JacksonXmlProperty(localName = "ZIP_CODE")
    @JacksonXmlCData
    @Length(max = 6, message = "邮政编码长度不能超过6")
    private String zipCode;

    @JacksonXmlProperty(isAttribute = true)
    @JsonIgnore
//    @XStreamOmitField
    @XStreamAsAttribute
    private String type = "G";

}
