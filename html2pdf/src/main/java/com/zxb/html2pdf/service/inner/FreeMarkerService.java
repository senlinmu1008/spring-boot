/**
 * Copyright (C), 2015-2019
 */
package com.zxb.html2pdf.service.inner;

/**
 *
 * @author zhaoxb
 * @create 2019-09-30 15:08
 */
public interface FreeMarkerService {
    String getHtml(String templatePath, Object dataModel);
}
