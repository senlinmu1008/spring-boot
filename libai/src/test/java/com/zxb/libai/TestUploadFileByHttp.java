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
    @Test
    public void testUploadFileAsBinary1() {
        String responseBody = HttpFileTransferUtils.uploadFileAsBinary("http://127.0.0.1:11100/uploadFileAsBinary", "/Users/zhaoxiaobin/Documents/doc/阿里云/Java开发手册（嵩山版）.pdf");
        System.out.println(responseBody);
    }

    @Test
    public void testUploadFileAsBinary2() {
        Map<String, String> headers = new HashMap<>();
        headers.put("head1", "123");
        headers.put("head2", "456");
        headers.put("head3", "789");
        String responseBody = HttpFileTransferUtils.uploadFileAsBinary("http://127.0.0.1:11100/uploadFileAsBinary",
                ContentType.APPLICATION_OCTET_STREAM,
                headers,
                "/Users/zhaoxiaobin/Documents/doc/阿里云/Java开发手册（嵩山版）.pdf",
                DEFAULT_READ_TIMEOUT,
                Consts.UTF_8);
        System.out.println(responseBody);
    }

    @Test
    public void testUploadFileAsMultipart1() {
        Map<String, File> files = new HashMap<>();
        files.put("file1", new File("/Users/zhaoxiaobin/Documents/markdown/docs/16120108773457.md"));
        files.put("file2", new File("/Users/zhaoxiaobin/Documents/doc/阿里云/Java开发手册（嵩山版）.pdf"));

        Map<String, String> formData = new HashMap<>();
        formData.put("fileName1", "16120108773457.md");
        formData.put("fileName2", "Java开发手册（嵩山版）.pdf");
        String responseBody = HttpFileTransferUtils.uploadFileAsMultipart("http://127.0.0.1:11100/uploadFileAsMultipart",
                files,
                formData);
        System.out.println(responseBody);
    }

    @Test
    public void testUploadFileAsMultipart2() {
        Map<String, String> headers = new HashMap<>();
        headers.put("head1", "123");
        headers.put("head2", "456");
        headers.put("head3", "789");

        Map<String, File> files = new HashMap<>();
        files.put("file1", new File("/Users/zhaoxiaobin/Documents/markdown/docs/16120108773457.md"));
        files.put("file2", new File("/Users/zhaoxiaobin/Documents/doc/阿里云/Java开发手册（嵩山版）.pdf"));

        Map<String, String> formData = new HashMap<>();
        formData.put("fileName1", "16120108773457.md");
        formData.put("fileName2", "Java开发手册（嵩山版）.pdf");
        String responseBody = HttpFileTransferUtils.uploadFileAsMultipart("http://127.0.0.1:11100/uploadFileAsMultipart",
                headers,
                files,
                formData,
                DEFAULT_READ_TIMEOUT,
                Consts.UTF_8);
        System.out.println(responseBody);
    }

    @Test
    public void testByHutool() {
        HttpResponse httpResponse = HttpRequest.post("http://127.0.0.1:11100/uploadFileAsMultipart")
                .form("file1", new File("/Users/zhaoxiaobin/Documents/markdown/docs/16120108773457.md"))
                .form("file2", new File("/Users/zhaoxiaobin/Documents/doc/阿里云/Java开发手册（嵩山版）.pdf"))
                .form("fileName1", "16120108773457.md")
                .form("fileName2", "Java开发手册（嵩山版）.pdf")
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
        FileSystemResource fileSystemResource1 = new FileSystemResource("/Users/zhaoxiaobin/Documents/markdown/docs/16120108773457.md");
        FileSystemResource fileSystemResource2 = new FileSystemResource("/Users/zhaoxiaobin/Documents/doc/阿里云/Java开发手册（嵩山版）.pdf");
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file1", fileSystemResource1);
        form.add("file2", fileSystemResource2);
        form.add("fileName1", "16120108773457.md");
        form.add("fileName2", "Java开发手册（嵩山版）.pdf");
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        // 发起请求
        String httpResponse = restTemplate.postForObject("http://127.0.0.1:11100/uploadFileAsMultipart", files, String.class);
        System.out.println(httpResponse);
    }
}