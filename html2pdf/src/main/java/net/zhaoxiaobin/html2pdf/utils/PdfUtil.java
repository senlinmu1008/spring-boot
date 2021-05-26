/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.html2pdf.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import jodd.util.Base64;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * pdf操作的工具类
 *
 * @author zhaoxb
 * @create 2019-11-25 10:28
 */
public class PdfUtil {

    /**
     * 水平旋转pdf，只有当前页实际是水平放置（rotation为0或180）的时候才旋转90°
     * 结果也是以Base64字符串形式返回
     *
     * @param sourceBase64 pdf源文件（Base64字符形式）
     * @return
     */
    public static String horRotate(String sourceBase64) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        horRotate(Base64.decode(sourceBase64), out);
        return Base64.encodeToString(out.toByteArray());
    }

    /**
     * 水平旋转pdf，只有当前页实际是水平放置（rotation为0或180）的时候才旋转90°
     *
     * @param sourcePdfPath 源文件路径
     * @param targetPdfPath 结果文件路径
     */
    @SneakyThrows
    public static void horRotate(String sourcePdfPath, String targetPdfPath) {
        @Cleanup FileOutputStream out = new FileOutputStream(targetPdfPath);
        horRotate(FileUtils.readFileToByteArray(new File(sourcePdfPath)), out);
    }

    /**
     * 水平旋转pdf，只有当前页实际是水平放置（rotation为0或180）的时候才旋转90°
     *
     * @param sourceBytes pdf源文件（byte数组形式）
     * @param out         输出流
     */
    @SneakyThrows
    public static void horRotate(byte[] sourceBytes, OutputStream out) {
        @Cleanup PdfReader reader = new PdfReader(sourceBytes); // 源文件
        @Cleanup Document document = new Document(); // 目标文件,默认A4纵向
        @Cleanup PdfCopy pc = new PdfSmartCopy(document, out); // 生成的目标PDF文件
        document.open();

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            Rectangle pageSize = reader.getPageSizeWithRotation(i); // 获取页面宽高
            int rotation = pageSize.getRotation(); // 页面旋转角度
            if (pageSize.getWidth() > pageSize.getHeight()) {
                PdfDictionary pd = reader.getPageN(i);
                pd.put(PdfName.ROTATE, new PdfNumber((rotation == 0 || rotation == 180) ? 90 : 0)); // 只有是水平放置的情况才转90°
            }
            pc.addPage(pc.getImportedPage(reader, i));
        }
    }

    /**
     * pdf -> image -> pdf
     * 主要是为了解决pdf预览兼容性问题
     *
     * @param sourceBase64 pdf源文件（Base64字符形式）
     * @return
     */
    public static String pdf2Image(String sourceBase64) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdf2Image(Base64.decode(sourceBase64), out);
        return Base64.encodeToString(out.toByteArray());
    }

    /**
     * pdf -> image -> pdf
     * 主要是为了解决pdf预览兼容性问题
     *
     * @param sourcePdfPath 源文件路径
     * @param targetPdfPath 结果文件路径
     */
    @SneakyThrows
    public static void pdf2Image(String sourcePdfPath, String targetPdfPath) {
        @Cleanup FileOutputStream out = new FileOutputStream(targetPdfPath);
        pdf2Image(FileUtils.readFileToByteArray(new File(sourcePdfPath)), out);
    }

    /**
     * pdf -> image -> pdf
     * 主要是为了解决pdf预览兼容性问题
     *
     * @param sourceBytes pdf源文件（byte数组形式）
     * @param out         输出流
     */
    @SneakyThrows
    public static void pdf2Image(byte[] sourceBytes, OutputStream out) {
        @Cleanup PDDocument document = PDDocument.load(sourceBytes);
        PDFRenderer renderer = new PDFRenderer(document);
        @Cleanup Document newDocument = new Document();
        PdfWriter.getInstance(newDocument, out);
        newDocument.open();
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            // 生成图片（内存）
            BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            // 将图片添加到pdf中
            Image image = Image.getInstance(baos.toByteArray());
            image.setAbsolutePosition(0, 0);
            image.scaleAbsolute(PageSize.A4);
            newDocument.newPage();
            newDocument.add(image);
        }
    }
}