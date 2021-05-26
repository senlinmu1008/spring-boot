package net.zhaoxiaobin.html2pdf.domain;

import lombok.Data;

/**
 * 生成pdf文件响应实体类
 *
 * @author zhaoxb
 * @create 2019-03-21 12:54
 */
@Data
public class GeneratePdfResp {
    /**
     * 生成pdf的绝对路径
     */
    private String absolutePath;
}
