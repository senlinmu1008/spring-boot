/**
 * Copyright (C), 2015-2019
 */
package com.zxb.html2pdf.service.inner;


import com.zxb.html2pdf.domain.WaterMarkInfo;

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
