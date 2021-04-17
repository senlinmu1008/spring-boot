package com.zxb.libai;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author zhaoxb
 * @date 2021/04/17 3:35 下午
 */
public class TestUploadFileByHttp {
    @Test
    @SneakyThrows
    public void test1() {
        @Cleanup CloseableHttpClient ht = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://127.0.0.1:11100/uploadFile");
        HttpResponse rs;
        InputStreamEntity inputStreamEntity = null;
        try {
            File testFile = new File("/Users/zhaoxiaobin/Documents/.rar/71.zip");
            System.out.println(testFile.exists());
            //文件流包装到FileBody
            inputStreamEntity = new InputStreamEntity(new FileInputStream(testFile), testFile.length());
            post.setEntity(inputStreamEntity);
            //设置请求内容类型（若不显式地设置，默认text/plain;不同的类型服务端解析格式不同，可能导致参请求参数解析不到的情况）
            post.addHeader("Content-Type", "application/octet-stream");
            //发送请求
            rs = ht.execute(post);
            System.out.println("" + rs.getStatusLine().getStatusCode() + " " + EntityUtils.toString(rs.getEntity(), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 销毁
            EntityUtils.consume(inputStreamEntity);
        }
    }

    @Test
    @SneakyThrows
    public void test2() {
        @Cleanup CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:11100/uploadMultipartFile");

//        方式一
//        MultipartEntityBuil der builder = Multi partEntityBuilder.create();
////        builder.addTextBody("username", "John");
////        builder.addTextBody("password", "pass");
//        builder.addBinaryBody("file",
//                new File("/Users/zhaoxiaobin/Documents/tienon/nginx/nginx.conf"), ContentType.APPLICATION_OCTET_STREAM, "file.ext");
//        HttpEntity multipart = builder.build();

//        方式二
//        StringBody userName = new StringBody("Scott", ContentType.create(
//                "text/plain", Consts.UTF_8));
//        StringBody password = new StringBody("123456", ContentType.create(
//                "text/plain", Consts.UTF_8));
        HttpEntity multipart = MultipartEntityBuilder.create()
                // 相当于<input type="file" name="file"/>
                .addPart("file", new FileBody(new File("/Users/zhaoxiaobin/Documents/tienon/nginx/nginx.conf")))
                // 相当于<input type="text" name="userName" value=userName>
//                .addPart("userName", userName)
//                .addPart("pass", password)
                .build();

        httpPost.setEntity(multipart);
        @Cleanup CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        System.out.println(EntityUtils.toString(responseEntity));
        EntityUtils.consume(responseEntity);
    }
}