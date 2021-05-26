/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.html2pdf.service.inner;


import net.zhaoxiaobin.html2pdf.domain.WaterMarkInfo;

import java.io.OutputStream;

/**
 *
 * @author zhaoxb
 * @create 2019-10-01 21:34
 */
public interface PdfService {
    byte[] html2Pdf(String html, float width, float height, WaterMarkInfo waterMarkInfo);

    void html2Pdf(String html, float width, float height, OutputStream out);
}
