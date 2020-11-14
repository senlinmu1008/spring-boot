/**
 * Copyright (C), 2015-2019
 */
package com.zxb.html2pdf.service.impl.inner;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.zxb.html2pdf.domain.WaterMarkInfo;
import com.zxb.html2pdf.exception.PdfBizException;
import com.zxb.html2pdf.exception.PdfErrorCode;
import com.zxb.html2pdf.service.inner.PdfService;
import com.zxb.html2pdf.service.inner.WaterMarkerService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * html转pdf业务类
 *
 * @author zhaoxb
 * @create 2019-10-01 22:05
 */
@Service("pdfService")
@Slf4j
public class PdfServiceImpl implements PdfService {
    public static final String FONT_PATH = "fonts/simsun.ttc,1";

    @Autowired
    private WaterMarkerService waterMarkerService;

    /**
     * html页面内容转pdf，并给每页附上水印
     *
     * @param html          html页面内容
     * @param width         pdf的宽
     * @param height        pdf的高
     * @param waterMarkInfo 水印信息
     * @return
     */
    @Override
    public byte[] html2Pdf(String html, float width, float height, WaterMarkInfo waterMarkInfo) {
        log.info("=================开始将html转换为pdf=================");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.html2Pdf(html, width, height, out);
        byte[] bytes = out.toByteArray();
        // 设置水印
        if (waterMarkInfo != null) {
            bytes = waterMarkerService.addWaterMarker(bytes, waterMarkInfo);
        }
        return bytes;
    }

    /**
     * html转pdf
     *
     * @param html   html页面内容
     * @param width  pdf的宽
     * @param height pdf的高
     * @param out    输出流，pdf文件用此流输出，需要pdf文档关闭后流中才会有数据
     */
    @Override
    @SneakyThrows
    public void html2Pdf(String html, float width, float height, OutputStream out) {
        @Cleanup Document document = new Document(new RectangleReadOnly(width, height)); // 默认A4纵向
        // 这里需要关闭document才能让生成的pdf字节数据刷到输出流中
        PdfWriter writer = PdfWriter.getInstance(document, out); // 关闭可能导致生成的pdf显示异常（Chrome）
        document.open();
        // 设置字体,这里统一用simsun.ttc即宋体
        XMLWorkerFontProvider asianFontProvider = new XMLWorkerFontProvider() {
            @Override
            public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
                Font font;
                try {
                    font = new Font(BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                } catch (Exception e) {
                    log.error(PdfErrorCode.SET_PDF_FONT_FAIL.getDesc(), e);
                    throw new PdfBizException(PdfErrorCode.SET_PDF_FONT_FAIL);
                }
                font.setStyle(style);
                font.setColor(color);
                if (size > 0) {
                    font.setSize(size);
                }
                return font;
            }
        };

        // 生成pdf
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes("UTF-8")), null, Charset.forName("UTF-8"), asianFontProvider);

            // 如果系统已经装有simsun.ttc字体，则不需要单独设置字体也不需要itext-asian jar包
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes("UTF-8")), null, Charset.forName("UTF-8"));
        } catch (RuntimeWorkerException e) {
            log.error(PdfErrorCode.HTML_CONVERT2PDF_FAIL.getDesc(), e);
            throw new PdfBizException(PdfErrorCode.HTML_CONVERT2PDF_FAIL);
        }
    }

}