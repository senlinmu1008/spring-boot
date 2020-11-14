package com.zxb.html2pdf.exception;

import lombok.Data;

/**
 * 自定义异常
 *
 * @author zhaoxb
 * @date 2019-09-30 15:31
 * @return
 */
@Data
public class PdfBizException extends RuntimeException {

    private String retCode;

    private String retMsg;

    public PdfBizException(PdfErrorCode errorCode) {
        super(errorCode.getDesc());
        this.retCode = errorCode.getValue();
        this.retMsg = errorCode.getDesc();
    }

    public PdfBizException(String desc) {
        super(desc);
    }

    public PdfBizException(String retCode, String retMsg) {
        super(retMsg);
        this.retCode = retCode;
        this.retMsg = retMsg;
    }
}
