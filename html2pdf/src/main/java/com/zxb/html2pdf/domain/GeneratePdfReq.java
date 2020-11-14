package com.zxb.html2pdf.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 生成pdf文件请求实体类
 *
 * @author zhaoxb
 * @create 2019-03-21 11:59
 */
@Data
public class GeneratePdfReq {
    /**
     * 生成pdf文件的绝对路径
     */
    @NotBlank(message = "生成pdf文件的绝对路径不能为空")
    @Pattern(regexp = "^.*(\\.pdf|\\.jpg)$", message = "生成的文件必须以.pdf或.jpg结尾")
    private String absolutePath;
    /**
     * 使用html模板的绝对路径
     */
    @NotBlank(message = "使用的模板路径不能为空")
    private String templateName;
    /**
     * 渲染模板的业务数据
     */
    private Object dataModel;
    /**
     * 水印信息
     */
    private WaterMarkInfo waterMarkInfo;
    /**
     * pdf文件的宽，默认A4
     */
    private float width = 595;
    /**
     * pdf文件的高，默认A4
     */
    private float height = 842;
}
