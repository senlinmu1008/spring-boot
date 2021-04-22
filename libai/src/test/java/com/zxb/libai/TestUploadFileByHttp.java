package com.zxb.libai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zxb.libai.utils.HttpFileTransferUtils;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.zxb.libai.utils.HttpFileTransferUtils.DEFAULT_READ_TIMEOUT;

/**
 * @author zhaoxb
 * @date 2021/04/17 3:35 下午
 */
public class TestUploadFileByHttp {
    private String uploadFileAsBinaryUrl = "http://127.0.0.1:11100/uploadFileAsBinary";
    
    private String uploadFileAsMultipart = "http://127.0.0.1:11100/uploadFileAsMultipart";
    
    private String filePath1 = "/Users/zhaoxiaobin/Documents/markdown/docs/16120108773457.md";
    
    private String filePath2 = "/Users/zhaoxiaobin/Documents/doc/阿里云/Java开发手册（嵩山版）.pdf";
    
    @Test
    public void testUploadFileAsBinary1() {
        String responseBody = HttpFileTransferUtils.uploadFileAsBinary(uploadFileAsBinaryUrl, filePath2);
        System.out.println(responseBody);
    }

    @Test
    public void testUploadFileAsBinary2() {
        Map<String, String> headers = new HashMap<>();
        headers.put("head1", "123");
        headers.put("head2", "456");
        headers.put("head3", "789");
        String responseBody = HttpFileTransferUtils.uploadFileAsBinary(uploadFileAsBinaryUrl,
                ContentType.APPLICATION_OCTET_STREAM,
                headers,
                filePath2,
                DEFAULT_READ_TIMEOUT,
                Consts.UTF_8);
        System.out.println(responseBody);
    }

    @Test
    public void testUploadFileAsMultipart1() {
        Map<String, File> files = new HashMap<>();
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        files.put("file1", file1);
        files.put("file2", file2);

        Map<String, String> formData = new HashMap<>();
        formData.put("fileName1", file1.getName());
        formData.put("fileName2", file2.getName());
        String responseBody = HttpFileTransferUtils.uploadFileAsMultipart(uploadFileAsMultipart, files, formData);
        System.out.println(responseBody);
    }

    @Test
    public void testUploadFileAsMultipart2() {
        Map<String, String> headers = new HashMap<>();
        headers.put("head1", "123");
        headers.put("head2", "456");
        headers.put("head3", "789");

        Map<String, File> files = new HashMap<>();
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        files.put("file1", file1);
        files.put("file2", file2);

        Map<String, String> formData = new HashMap<>();
        formData.put("fileName1", file1.getName());
        formData.put("fileName2", file2.getName());
        String responseBody = HttpFileTransferUtils.uploadFileAsMultipart(uploadFileAsMultipart,
                headers,
                files,
                formData,
                DEFAULT_READ_TIMEOUT,
                Consts.UTF_8);
        System.out.println(responseBody);
    }

    @Test
    public void testByHutool() {
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        HttpResponse httpResponse = HttpRequest.post(uploadFileAsMultipart)
                .form("file1", file1)
                .form("file2", file2)
                .form("fileName1", file1.getName())
                .form("fileName2", file2.getName())
                .execute();
        System.out.println(httpResponse.body());
    }

    @Test
    public void testByRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // 设置请求体，注意是LinkedMultiValueMap
        FileSystemResource fileSystemResource1 = new FileSystemResource(filePath1);
        FileSystemResource fileSystemResource2 = new FileSystemResource(filePath2);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file1", fileSystemResource1);
        form.add("file2", fileSystemResource2);
        form.add("fileName1", fileSystemResource1.getFilename());
        form.add("fileName2", fileSystemResource2.getFilename());
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        // 发起请求
        String httpResponse = restTemplate.postForObject(uploadFileAsMultipart, files, String.class);
        System.out.println(httpResponse);
    }
}