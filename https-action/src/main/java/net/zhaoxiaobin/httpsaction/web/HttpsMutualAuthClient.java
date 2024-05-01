package net.zhaoxiaobin.httpsaction.web;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * https双向认证请求客户端demo
 * <p>
 * 生成p12证书
 * openssl genrsa -out client.key 2048
 * openssl req -new -x509 -key client.key -out client.crt -days 36500
 * openssl pkcs12 -export -clcerts -in client.crt -inkey client.key -out client.p12 -passout pass:123456 -name clientp12
 * 生成服务端信任库
 * keytool -import -trustcacerts -file server.crt -keystore server.truststore -storepass 123456 -alias server
 *
 * @author zhaoxb
 * @date 2023-04-12 下午5:22
 */
@RestController
public class HttpsMutualAuthClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpsMutualAuthClient.class);

    // 客户端p12证书（根据实际修改）
    private String p12Path = "/Users/zhaoxiaobin/Desktop/client.p12";

    // 客户端p12证书密码（根据实际修改）
    private String p12Pwd = "123456";

    // 信任库（服务端证书）（根据实际修改）
    private String trustStoreFile = "/Users/zhaoxiaobin/Desktop/server.truststore";

    // 信任库密码（根据实际修改）
    private String trustStorePwd = "123456";

    // url（根据实际修改），需要使用域名访问，可在本地hosts中配置
    private String url = "https://test.zhaoxiaobin.net:49999/test/queryTxDetail";

    /**
     * 使用httpclient-4.5.14
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/httpsMutualAuthByHttpClient")
    @Test
    public void httpsMutualAuthByHttpClient() throws Exception {
        // 1.初始化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = this.getKeyStore(p12Path, p12Pwd, "PKCS12");
        keyManagerFactory.init(keyStore, p12Pwd.toCharArray());
        // 2.初始化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustKeyStore = this.getKeyStore(trustStoreFile, trustStorePwd, "JKS");
        trustManagerFactory.init(trustKeyStore);
        // 3.初始化ssl
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        // 4.发送https请求
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(30000).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/json");
            String requestBody = "{\"name\": \"zhaoxb\"}";
            StringEntity requestEntity = new StringEntity(requestBody, StandardCharsets.UTF_8);
            httpPost.setEntity(requestEntity);
            httpResponse = httpClient.execute(httpPost);
            // 获取响应并判断响应码
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity, "UTF-8");
            EntityUtils.consume(responseEntity);
            logger.info("响应报文:{}", responseBody);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("请求失败,http响应码:{}", statusCode);
                throw new RuntimeException("请求失败");
            }
            System.out.println(responseBody);
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
    }

    /**
     * 使用hutool-all-5.8.12
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/httpsMutualAuthByHutool")
    @Test
    public void httpsMutualAuthByHutool() throws Exception {
        // 1.初始化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = this.getKeyStore(p12Path, p12Pwd, "PKCS12");
        keyManagerFactory.init(keyStore, p12Pwd.toCharArray());
        // 2.初始化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustKeyStore = this.getKeyStore(trustStoreFile, trustStorePwd, "JKS");
        trustManagerFactory.init(trustKeyStore);
        // 3.初始化ssl
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        // 4.发送https请求
        HttpResponse httpResponse = HttpRequest.post(url)
                .setSSLSocketFactory(sslSocketFactory)
                .setConnectionTimeout(5000)
                .setReadTimeout(30000)
                .contentType(ContentType.JSON.toString())
                .body("{\"name\": \"zhaoxb\"}")
                .charset(CharsetUtil.CHARSET_UTF_8)
                .execute();
        // 获取响应并判断响应码
        String responseBody = httpResponse.body();
        logger.info("响应报文:{}", responseBody);
        if (!httpResponse.isOk()) {
            logger.error("请求失败,http响应码:{}", httpResponse.getStatus());
            throw new RuntimeException("请求失败");
        }
        System.out.println(responseBody);
    }

    /**
     * 获得KeyStore
     *
     * @param keyStoreFilePath 密钥库文件路径
     * @param keyStoreFilePwd  密钥库文件密码
     * @param type             密钥库文件类型 PKCS12/JKS/....
     * @return
     * @throws Exception
     */
    private KeyStore getKeyStore(String keyStoreFilePath, String keyStoreFilePwd, String type) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(type);
        InputStream instream = null;
        try {
            instream = new FileInputStream(keyStoreFilePath);
            keyStore.load(instream, keyStoreFilePwd.toCharArray());
        } finally {
            if (instream != null) {
                instream.close();
            }
        }
        return keyStore;
    }
}