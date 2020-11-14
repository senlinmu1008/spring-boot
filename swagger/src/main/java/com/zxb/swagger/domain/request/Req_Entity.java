package com.zxb.swagger.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class Req_Entity {
    @ApiModelProperty(value = "区县代码", example = "310115")
    @XStreamAlias("DISTRICT_ID")
    @JsonProperty("DISTRICT_ID")
    @JacksonXmlProperty(localName = "DISTRICT_ID")
    @JacksonXmlCData
    @Length(max = 6, message = "区县代码长度不能超过6")
    private String districtId;

    @ApiModelProperty(value = "经办机构编号", example = "310000000")
    @XStreamAlias("ORG_ID")
    @JsonProperty("ORG_ID")
    @JacksonXmlProperty(localName = "ORG_ID")
    @JacksonXmlCData
    @Length(max = 9, message = "经办机构编号长度不能超过9")
    private String orgId;

    @ApiModelProperty(value = "用户姓名", example = "zhaoxb", required = true)
    @NotBlank(message = "用户姓名不能为空")
    @XStreamAlias("USER_NAME")
    @JsonProperty("USER_NAME")
    @JacksonXmlProperty(localName = "USER_NAME")
    @JacksonXmlCData
    @Length(max = 180, message = "用户姓名长度不能超过180")
    private String userName;

    @ApiModelProperty(value = "出生日期", example = "19900307")
    @XStreamAlias("BIRTH_DAY")
    @JsonProperty("BIRTH_DAY")
    @JacksonXmlProperty(localName = "BIRTH_DAY")
    @JacksonXmlCData
    @Length(max = 8, message = "出生日期长度不能超过8")
    private String birthDay;

    @ApiModelProperty(value = "证件号码", example = "110101199003079833", required = true)
    @NotBlank(message = "证件号码不能为空")
    @XStreamAlias("ID_NO")
    @JsonProperty("ID_NO")
    @JacksonXmlProperty(localName = "ID_NO")
    @JacksonXmlCData
    @Length(max = 18, message = "证件号码长度不能超过18")
    private String idNo;

    @ApiModelProperty(value = "手机号码", example = "13485527323", required = true)
    @NotBlank(message = "移动电话号码不能为空")
    @XStreamAlias("TEL_PHONE")
    @JsonProperty("TEL_PHONE")
    @JacksonXmlProperty(localName = "TEL_PHONE")
    @JacksonXmlCData
    @Length(max = 240, message = "移动电话号码长度不能超过240")
    private String telPhone;

    @ApiModelProperty(value = "请求详细信息")
    @Valid
    @XStreamAlias("DETAILS")
    @XStreamImplicit(itemFieldName = "DETAILS")
    @JsonProperty("DETAILS")
    @JacksonXmlProperty(localName = "DETAILS")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Req_Detail> details;

}
