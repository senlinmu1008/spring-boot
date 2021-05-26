package net.zhaoxiaobin.libai.web;

import cn.hutool.core.io.FileUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

/**
 * @author zhaoxb
 * @date 2021/04/17 3:36 下午
 */
@RestController
@Slf4j
public class UploadFileController {
    private String basePath = "/Users/zhaoxiaobin/Desktop/";

    @PostMapping(value = "/uploadFileAsBinary", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String uploadFileAsBinary(@RequestHeader HttpHeaders headers, HttpServletRequest request) throws IOException {
        // 打印请求头
        headers.forEach((key, list) -> System.out.println(key + ":" + list.stream().collect(Collectors.joining(","))));
        // 写文件
        @Cleanup ServletInputStream inputStream = request.getInputStream();
        FileUtil.writeFromStream(inputStream, basePath + System.currentTimeMillis() + ".pdf");
        return "uploadFileAsBinary 成功";
    }

    @PostMapping(value = "/uploadFileAsMultipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFileAsMultipart(@RequestHeader HttpHeaders headers, MultipartFile file1, String fileName1, MultipartFile file2, String fileName2) throws IOException {
        // 打印请求头
        headers.forEach((key, list) -> System.out.println(key + ":" + list.stream().collect(Collectors.joining(","))));
        // 写文件
        @Cleanup InputStream inputStream1 = file1.getInputStream();
        FileUtil.writeFromStream(inputStream1, basePath + fileName1);
        @Cleanup InputStream inputStream2 = file2.getInputStream();
        FileUtil.writeFromStream(inputStream2, basePath + fileName2);
        return "uploadFileAsMultipart 成功";
    }
}