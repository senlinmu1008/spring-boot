package net.zhaoxiaobin.httpsaction.web;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
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
 * https客户端双向认证代码
 *
 * @author zhaoxb
 * @date 2023-04-04 下午2:36
 */
@RestController
@Slf4j
public class HttpsClientMutualAuthController {

    // 1.1服务端、客户端都是自签名证书
    @RequestMapping("/httpsMutualAuthByHttpClient11")
    public String httpsMutualAuthByHttpClient11() throws Exception {
        // 客户端p12证书
        String p12Path = "/certificates/client/client.p12";
        // 客户端p12证书密码
        String p12Pwd = "123456";
        // 信任库（服务端证书）
        String trustStoreFile = "/certificates/server/server.truststore";
        // 信任库密码
        String trustStorePwd = "123456";
        // url，需要使用域名访问，可在本地hosts中配置域名解析
        String url = "https://zhaoxiaobin.test.com:21000/hello";

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
            httpPost.addHeader("Content-Type", "text/plain");
            String requestBody = "Https双向认证（HttpClient）";
            StringEntity requestEntity = new StringEntity(requestBody, StandardCharsets.UTF_8);
            httpPost.setEntity(requestEntity);
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("请求失败,http响应码:{}", httpResponse.getStatusLine().getStatusCode());
                throw new RuntimeException("请求失败");
            }
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity, "UTF-8");
            EntityUtils.consume(responseEntity);
            return responseBody;
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
    }

    // 1.2服务端、客户端都是自签名证书
    @RequestMapping("/httpsMutualAuthByHutool12")
    public String httpsMutualAuthByHutool12() throws Exception {
        // 客户端p12证书
        String p12Path = "/certificates/client/client.p12";
        // 客户端p12证书密码
        String p12Pwd = "123456";
        // 信任库（服务端证书）
        String trustStoreFile = "/certificates/server/server.truststore";
        // 信任库密码
        String trustStorePwd = "123456";
        // url，需要使用域名访问，可在本地hosts中配置域名解析
        String url = "https://zhaoxiaobin.test.com:21000/hello";

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
                .contentType(ContentType.TEXT_PLAIN.toString())
                .body("Https双向认证（Hutool）")
                .charset(CharsetUtil.CHARSET_UTF_8)
                .execute();
        if (!httpResponse.isOk()) {
            log.error("请求失败,响应信息:{}", httpResponse.body());
            throw new RuntimeException("请求失败");
        }
        return httpResponse.body();
    }

    // 2.1服务端CA证书、客户端自签名证书
    @RequestMapping("/httpsMutualAuthByHttpClient21")
    public String httpsMutualAuthByHttpClient21() throws Exception {
        // 客户端p12证书
        String p12Path = "/certificates/client/client.p12";
        // 客户端p12证书密码
        String p12Pwd = "123456";
        // url，需要使用域名访问，可在本地hosts中配置域名解析
        String url = "https://zhaoxiaobin.net:22000/hello";

//        // 1.初始化密钥库
//        KeyStore keyStore = this.getKeyStore(p12Path, p12Pwd, "PKCS12");
//        // 2.初始化ssl
//        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, p12Pwd.toCharArray()).build();

        // 1.初始化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = this.getKeyStore(p12Path, p12Pwd, "PKCS12");
        keyManagerFactory.init(keyStore, p12Pwd.toCharArray());
        // 2.初始化ssl
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

        // 双向认证这里trustManagers为空则使用JDK默认的cacerts信任库
        sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        // 3.发送https请求
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(30000).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "text/plain");
            String requestBody = "Https双向认证（HttpClient）";
            StringEntity requestEntity = new StringEntity(requestBody, StandardCharsets.UTF_8);
            httpPost.setEntity(requestEntity);
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("请求失败,http响应码:{}", httpResponse.getStatusLine().getStatusCode());
                throw new RuntimeException("请求失败");
            }
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity, "UTF-8");
            EntityUtils.consume(responseEntity);
            return responseBody;
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
    }

    // 2.2服务端CA证书、客户端自签名证书
    @RequestMapping("/httpsMutualAuthByHutool22")
    public String httpsMutualAuthByHutool22() throws Exception {
        // 客户端p12证书
        String p12Path = "/certificates/client/client.p12";
        // 客户端p12证书密码
        String p12Pwd = "123456";
        // url，需要使用域名访问，可在本地hosts中配置域名解析
        String url = "https://zhaoxiaobin.net:22000/hello";

        // 1.初始化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = this.getKeyStore(p12Path, p12Pwd, "PKCS12");
        keyManagerFactory.init(keyStore, p12Pwd.toCharArray());
        // 2.初始化ssl
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        // 双向认证这里trustManagers为空则使用JDK默认的cacerts信任库
        sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        // 3.发送https请求
        HttpResponse httpResponse = HttpRequest.post(url)
                .setSSLSocketFactory(sslSocketFactory)
                .setConnectionTimeout(5000)
                .setReadTimeout(30000)
                .contentType(ContentType.TEXT_PLAIN.toString())
                .body("Https双向认证（Hutool）")
                .charset(CharsetUtil.CHARSET_UTF_8)
                .execute();
        if (!httpResponse.isOk()) {
            log.error("请求失败,响应信息:{}", httpResponse.body());
            throw new RuntimeException("请求失败");
        }
        return httpResponse.body();
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