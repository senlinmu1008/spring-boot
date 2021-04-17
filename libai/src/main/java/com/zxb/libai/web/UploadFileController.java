package com.zxb.libai.web;

import cn.hutool.core.io.FileUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhaoxb
 * @date 2021/04/17 3:36 下午
 */
@RestController
@Slf4j
public class UploadFileController {
    @PostMapping("/uploadMultipartFile")
    public String uploadMultipartFile(MultipartFile file) throws IOException {
        @Cleanup InputStream inputStream = file.getInputStream();
        FileUtil.writeFromStream(inputStream, "/Users/zhaoxiaobin/Desktop/" + System.currentTimeMillis() + ".txt");
        return "uploadMultipartFile success";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(HttpServletRequest request) throws IOException {
        @Cleanup ServletInputStream inputStream = request.getInputStream();
        FileUtil.writeFromStream(inputStream, "/Users/zhaoxiaobin/Desktop/" + System.currentTimeMillis() + ".txt");
        return "uploadFile success";
    }
}