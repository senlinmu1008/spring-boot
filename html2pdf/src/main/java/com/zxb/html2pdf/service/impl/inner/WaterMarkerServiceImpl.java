/**
 * Copyright (C), 2015-2019
 */
package com.zxb.html2pdf.service.impl.inner;

import cn.hutool.json.JSONUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zxb.html2pdf.domain.WaterMarkInfo;
import com.zxb.html2pdf.service.inner.WaterMarkerService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @author zhaoxb
 * @create 2019-10-02 15:06
 */
@Service("waterMarkerService")
@Slf4j
public class WaterMarkerServiceImpl implements WaterMarkerService {

    /**
     * 给pdf文件每页添加水印
     *
     * @param source        pdf文件的字节数组形式
     * @param waterMarkInfo 水印信息
     * @return
     */
    @Override
    public byte[] addWaterMarker(byte[] source, WaterMarkInfo waterMarkInfo) {
        log.info("开始设置水印数据{}", JSONUtil.toJsonPrettyStr(waterMarkInfo));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.addWaterMarker(source, waterMarkInfo, out);
        return out.toByteArray();
    }

    /**
     * 给pdf文件每页添加水印
     *
     * @param source        pdf文件的字节数组形式
     * @param waterMarkInfo 水印信息
     * @param out           输出流，pdf文件用此流输出，需要pdf文档关闭后流中才会有数据
     */
    @Override
    @SneakyThrows
    public void addWaterMarker(byte[] source, WaterMarkInfo waterMarkInfo, OutputStream out) {
        @Cleanup PdfReader reader = new PdfReader(source);
        // 这里需要关闭PdfStamper才能让生成的pdf字节数据刷到输出流中
        @Cleanup PdfStamper pdfStamper = new PdfStamper(reader, out);
        BaseFont font = BaseFont.createFont(waterMarkInfo.getFontName(), waterMarkInfo.getEncoding(), BaseFont.EMBEDDED);
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(waterMarkInfo.getOpacity());
        // 给每页pdf生成水印
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfContentByte waterMarker = pdfStamper.getUnderContent(i);
            waterMarker.beginText();
            // 设置水印透明度
            waterMarker.setGState(gs);
            // 设置水印字体和大小
            waterMarker.setFontAndSize(font, waterMarkInfo.getFontSize());
            // 设置水印位置、内容、旋转角度
            float X = reader.getPageSize(i).getWidth() * waterMarkInfo.getX() / 100;
            float Y = reader.getPageSize(i).getHeight() * waterMarkInfo.getY() / 100;
            waterMarker.showTextAligned(Element.ALIGN_CENTER, waterMarkInfo.getWaterMark(), X, Y, waterMarkInfo.getRotation());
            // 设置水印颜色
            waterMarker.setColorFill(BaseColor.GRAY);
            waterMarker.endText();
        }
    }
}