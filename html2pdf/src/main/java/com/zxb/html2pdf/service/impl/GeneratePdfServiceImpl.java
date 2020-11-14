package com.zxb.html2pdf.service.impl;

import cn.hutool.json.JSONUtil;
import com.zxb.html2pdf.domain.GeneratePdfReq;
import com.zxb.html2pdf.domain.GeneratePdfResp;
import com.zxb.html2pdf.service.RestService;
import com.zxb.html2pdf.service.inner.FreeMarkerService;
import com.zxb.html2pdf.service.inner.PdfService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author zhaoxb
 * @create 2019-03-21 13:14
 */
@Slf4j
@Service("generatePdfService")
public class GeneratePdfServiceImpl implements RestService {
    @Autowired
    private FreeMarkerService freeMarkerService;

    @Autowired
    private PdfService pdfService;

    @Override
    @SneakyThrows
    public GeneratePdfResp service(GeneratePdfReq generatePdfReq) {
        log.info("开始生成pdf文件,请求报文:{}", JSONUtil.toJsonPrettyStr(generatePdfReq));
        /*
        1.根据freemarker模板填充业务数据获取完整的html字符串
         */
        String html = freeMarkerService.getHtml(generatePdfReq.getTemplateName(), generatePdfReq.getDataModel());

        /*
        2.生成pdf文件（内存）
         */
        byte[] bytes = pdfService.html2Pdf(html, generatePdfReq.getWidth(), generatePdfReq.getHeight(), generatePdfReq.getWaterMarkInfo());

        /*
        3.本地保存pdf文件
         */
        File targetFile = new File(generatePdfReq.getAbsolutePath());
        // 上级目录不存在则创建
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        // 根据不同文件名后缀生成对应文件
        if (generatePdfReq.getAbsolutePath().endsWith("pdf")) {
            FileUtils.writeByteArrayToFile(targetFile, bytes);
        } else {
            @Cleanup PDDocument document = PDDocument.load(bytes);
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, 150);// 只打第一页,dpi越大图片越高清也越耗时
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            FileUtils.writeByteArrayToFile(targetFile, baos.toByteArray());
        }
        log.info("文件本地保存完成,文件路径:[{}]", targetFile.getAbsolutePath());

        /*
        4.组织返回
         */
        GeneratePdfResp generatePdfResp = new GeneratePdfResp();
        generatePdfResp.setAbsolutePath(targetFile.getAbsolutePath());
        return generatePdfResp;
    }
}
