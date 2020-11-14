/**
 * Copyright (C), 2015-2019
 */
package com.zxb.html2pdf.service.inner;


import com.zxb.html2pdf.domain.WaterMarkInfo;

import java.io.OutputStream;

/**
 *
 * @author zhaoxb
 * @create 2019-10-02 15:06
 */
public interface WaterMarkerService {
    byte[] addWaterMarker(byte[] source, WaterMarkInfo waterMarkInfo);

    void addWaterMarker(byte[] source, WaterMarkInfo waterMarkInfo, OutputStream out);

}
