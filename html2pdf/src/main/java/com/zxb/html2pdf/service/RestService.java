package com.zxb.html2pdf.service;

import com.zxb.html2pdf.domain.GeneratePdfReq;
import com.zxb.html2pdf.domain.GeneratePdfResp;

/**
 * @author zhaoxb
 * @create 2019-03-21 13:13
 */
public interface RestService {
    GeneratePdfResp service(GeneratePdfReq req);
}
