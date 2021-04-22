package com.zxb.libai.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoxb
 * @date 2021/04/21 4:37 下午
 */
public class HttpFileTransferUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpFileTransferUtils.class);

    /**
     * 默认超时时间
     */
    public static final int DEFAULT_READ_TIMEOUT = 300000;

    /**
     * 以二进制形式上传文件
     *
     * @param url      请求地址
     * @param filePath 本地文件路径
     * @return
     */
    public static String uploadFileAsBinary(String url, String filePath) {
        return uploadFileAsBinary(url, ContentType.APPLICATION_OCTET_STREAM, new HashMap<>(), filePath, DEFAULT_READ_TIMEOUT, Consts.UTF_8);
    }

    /**
     * 以二进制形式上传文件
     *
     * @param url         请求地址
     * @param contentType 内容编码类型 默认application/octet-stream
     * @param filePath    本地文件路径
     * @return
     */
    public static String uploadFileAsBinary(String url, ContentType contentType, String filePath) {
        return uploadFileAsBinary(url, contentType, new HashMap<>(), filePath, DEFAULT_READ_TIMEOUT, Consts.UTF_8);
    }

    /**
     * 以二进制形式上传文件
     *
     * @param url         请求地址
     * @param filePath    本地文件路径
     * @param readTimeout 超时时间，单位：毫秒 默认300秒
     * @return
     */
    public static String uploadFileAsBinary(String url, String filePath, int readTimeout) {
        return uploadFileAsBinary(url, ContentType.APPLICATION_OCTET_STREAM, new HashMap<>(), filePath, readTimeout, Consts.UTF_8);
    }

    /**
     * 以二进制形式上传文件
     *
     * @param url      请求地址
     * @param filePath 本地文件路径
     * @param charset  字符集 默认UTF-8，http响应体编码优先取响应Entity中的字符集
     * @return
     */
    public static String uploadFileAsBinary(String url, String filePath, Charset charset) {
        return uploadFileAsBinary(url, ContentType.APPLICATION_OCTET_STREAM, new HashMap<>(), filePath, DEFAULT_READ_TIMEOUT, charset);
    }

    /**
     * 以二进制形式上传文件
     *
     * @param url         请求地址
     * @param contentType 内容编码类型 默认application/octet-stream
     * @param headers     请求头 默认为空
     * @param filePath    本地文件路径
     * @param readTimeout 超时时间，单位：毫秒 默认300秒
     * @param charset     字符集 默认UTF-8，http响应体编码优先取响应Entity中的字符集
     * @return
     */
    public static String uploadFileAsBinary(String url, ContentType contentType, Map<String, String> headers, String filePath, int readTimeout, Charset charset) {
        Map<String, File> files = new HashMap<>();
        files.put("file", new File(filePath));
        return uploadFile(false, url, contentType, headers, files, null, readTimeout, charset);
    }

    /**
     * multipart表单方式上传文件
     *
     * @param url   请求地址
     * @param files 本地文件map
     * @return
     */
    public static String uploadFileAsMultipart(String url, Map<String, File> files) {
        return uploadFileAsMultipart(url, new HashMap<>(), files, new HashMap<>(), DEFAULT_READ_TIMEOUT, Consts.UTF_8);
    }

    /**
     * multipart表单方式上传文件
     *
     * @param url         请求地址
     * @param files       本地文件map
     * @param readTimeout 超时时间，单位：毫秒 默认300秒
     * @return
     */
    public static String uploadFileAsMultipart(String url, Map<String, File> files, int readTimeout) {
        return uploadFileAsMultipart(url, new HashMap<>(), files, new HashMap<>(), readTimeout, Consts.UTF_8);
    }

    /**
     * multipart表单方式上传文件
     *
     * @param url     请求地址
     * @param files   本地文件map
     * @param charset 字符集 默认UTF-8，http响应体编码优先取响应Entity中的字符集
     * @return
     */
    public static String uploadFileAsMultipart(String url, Map<String, File> files, Charset charset) {
        return uploadFileAsMultipart(url, new HashMap<>(), files, new HashMap<>(), DEFAULT_READ_TIMEOUT, charset);
    }

    /**
     * multipart表单方式上传文件
     *
     * @param url      请求地址
     * @param files    本地文件map
     * @param formData 表单数据，K-V格式
     * @return
     */
    public static String uploadFileAsMultipart(String url, Map<String, File> files, Map<String, String> formData) {
        return uploadFileAsMultipart(url, new HashMap<>(), files, formData, DEFAULT_READ_TIMEOUT, Consts.UTF_8);
    }

    /**
     * multipart表单方式上传文件
     *
     * @param url         请求地址
     * @param files       本地文件map
     * @param formData    表单数据，K-V格式
     * @param readTimeout 超时时间，单位：毫秒 默认300秒
     * @return
     */
    public static String uploadFileAsMultipart(String url, Map<String, File> files, Map<String, String> formData, int readTimeout) {
        return uploadFileAsMultipart(url, new HashMap<>(), files, formData, readTimeout, Consts.UTF_8);
    }

    /**
     * multipart表单方式上传文件
     *
     * @param url      请求地址
     * @param files    本地文件map
     * @param formData 表单数据，K-V格式
     * @param charset  字符集 默认UTF-8，http响应体编码优先取响应Entity中的字符集
     * @return
     */
    public static String uploadFileAsMultipart(String url, Map<String, File> files, Map<String, String> formData, Charset charset) {
        return uploadFileAsMultipart(url, new HashMap<>(), files, formData, DEFAULT_READ_TIMEOUT, charset);
    }

    /**
     * multipart表单方式上传文件
     *
     * @param url         请求地址
     * @param headers     请求头 默认为空
     * @param files       本地文件map
     * @param formData    表单数据，K-V格式
     * @param readTimeout 超时时间，单位：毫秒 默认300秒
     * @param charset     字符集 默认UTF-8，http响应体编码优先取响应Entity中的字符集
     * @return
     */
    public static String uploadFileAsMultipart(String url, Map<String, String> headers, Map<String, File> files, Map<String, String> formData, int readTimeout, Charset charset) {
        return uploadFile(true, url, ContentType.create(ContentType.MULTIPART_FORM_DATA.getMimeType(), charset), headers, files, formData, readTimeout, charset);
    }

    /**
     * 上传文件，包括二进制流方式和multipart表单方式
     *
     * @param isMultipart 是否为multipart表单方式
     * @param url         请求地址
     * @param contentType 内容编码类型 默认multipart/form-data
     * @param headers     请求头 默认为空
     * @param files       本地文件map
     * @param formData    表单数据，K-V格式
     * @param readTimeout 超时时间，单位：毫秒 默认300秒
     * @param charset     字符集 默认UTF-8，http响应体编码优先取响应Entity中的字符集
     * @return
     */
    private static String uploadFile(boolean isMultipart, String url, ContentType contentType, Map<String, String> headers, Map<String, File> files, Map<String, String> formData, int readTimeout, Charset charset) {
        // 判断文件的有效性，是否存在、不能为目录
        files.forEach((param, localFile) -> {
            if (!localFile.exists()) {
                logger.error("文件:{}不存在", localFile);
                throw new RuntimeException("文件不存在");
            }
            if (localFile.isDirectory()) {
                logger.error("不能上传文件夹:{}", localFile);
                throw new RuntimeException("不支持上传文件夹");
            }
        });
        // 实例化请求客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(readTimeout).build();
        httpPost.setConfig(requestConfig);
        // 添加http请求头
        headers.forEach((key, value) -> httpPost.addHeader(key, value));
        HttpEntity httpEntity = null;
        // 判断上传方式
        if (isMultipart) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 添加文件
            files.forEach((key, value) -> multipartEntityBuilder.addBinaryBody(key, value));
            // 添加表单其它参数
            formData.forEach((key, value) -> multipartEntityBuilder.addTextBody(key, value, contentType));
            httpEntity = multipartEntityBuilder.build();
        }

        InputStream inputStream = null;
        CloseableHttpResponse httpResponse = null;
        try {
            // 如果是二进制方式上传
            if (!isMultipart) {
                File uploadFile = files.values().stream().findFirst().get();
                inputStream = new FileInputStream(uploadFile);
                httpEntity = new InputStreamEntity(inputStream, uploadFile.length(), contentType);
            }
            httpPost.setEntity(httpEntity);
            // 发起请求
            httpResponse = httpClient.execute(httpPost);
            // 获取响应内容
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity, charset);
            EntityUtils.consume(responseEntity);
            // 判断请求是否成功
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("请求失败,响应码:{},响应信息:{}", statusCode, responseBody);
                throw new RuntimeException("请求失败");
            }
            return responseBody;
        } catch (IOException e) {
            logger.error("请求失败", e);
            throw new RuntimeException("请求失败");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                logger.error("释放资源失败", e);
                throw new RuntimeException("释放资源失败");
            }
        }
    }
}