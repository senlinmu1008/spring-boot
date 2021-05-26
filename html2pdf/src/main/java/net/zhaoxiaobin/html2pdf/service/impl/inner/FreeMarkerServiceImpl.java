/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.html2pdf.service.impl.inner;

import cn.hutool.json.JSONUtil;
import net.zhaoxiaobin.html2pdf.exception.PdfBizException;
import net.zhaoxiaobin.html2pdf.exception.PdfErrorCode;
import net.zhaoxiaobin.html2pdf.service.inner.FreeMarkerService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.StringWriter;

/**
 * 渲染html业务类
 *
 * @author zhaoxb
 * @create 2019-09-30 15:13
 */
@Service("freeMarkerService")
@Slf4j
public class FreeMarkerServiceImpl implements FreeMarkerService {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 渲染html后获取整个页面内容
     *
     * @param templatePath 模板路径
     * @param dataModel    业务数据，一般以map形式传入
     * @return
     */
    @Override
    public String getHtml(String templatePath, Object dataModel) {
        log.info("开始将模板{}渲染为html,业务数据{}", templatePath, JSONUtil.toJsonPrettyStr(dataModel));
        Configuration cfg = freeMarkerConfigurer.getConfiguration();
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER); // freemaker异常时仍旧抛出，统一异常处理
        cfg.setClassicCompatible(true);// 不需要对null值预处理，否则需要在模板取值时判断是否存在，不然报错
        StringWriter stringWriter = new StringWriter();
        try {
            // 设置模板所在目录，绝对路径方式，不打进jar包
//            cfg.setDirectoryForTemplateLoading(new File(templatePath).getParentFile());
//            Template temp = cfg.getTemplate(new File(templatePath).getName());

            // 相对路径设置模板所在目录，模板打进jar包，默认就是resources目录下的/templates目录。
            cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            Template temp = cfg.getTemplate(templatePath);
            temp.process(dataModel, stringWriter);
        } catch (Exception e) {
            log.error(PdfErrorCode.PDF_TEMPLATE_RENDER_FAIL.getDesc(), e);
            throw new PdfBizException(PdfErrorCode.PDF_TEMPLATE_RENDER_FAIL);
        }
        return stringWriter.toString();
    }
}