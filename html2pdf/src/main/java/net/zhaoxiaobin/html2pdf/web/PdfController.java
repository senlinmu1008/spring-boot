package net.zhaoxiaobin.html2pdf.web;

import net.zhaoxiaobin.html2pdf.domain.GeneratePdfReq;
import net.zhaoxiaobin.html2pdf.domain.GeneratePdfResp;
import net.zhaoxiaobin.html2pdf.service.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxb
 * @create 2019-09-24 13:38
 */
@Slf4j
@RestController
public class PdfController {
    @Autowired
    private RestService generatePdfService;

    @PostMapping(value = "/html2Pdf")
    public GeneratePdfResp html2Pdf(@RequestBody @Validated GeneratePdfReq req) {
        GeneratePdfResp resp = generatePdfService.service(req);
        return resp;
    }

}
