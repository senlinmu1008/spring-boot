package com.zxb.libai;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * @author zhaoxb
 * @date 2021/01/05 10:19 上午
 */
public class SSLTestCase2 {
    /**
     * 客户端p12证书路径
     */
    private String pfxPath = "/Users/zhaoxiaobin/Documents/tienon/nginx/cert/clientc/clientc.p12";
    /**
     * 客户端p12证书密码
     */
    private String pfxPasswd = "654321";
    /**
     * 信任库路径
     */
    private String trustStroreFile = "/Users/zhaoxiaobin/Documents/tienon/nginx/cert/server_ssl/serverWithDomain.truststore";
    /**
     * 信任库密码
     */
    private String trustStorePwd = "654321";

    private String url = "https://c.p5.ccb.com:21000/p5-http-in/filetransfer/P54211001-2001";
//    private String url = "https://139.9.127.172:21000/p5-http-in/filetransfer/P54211001-2001";

    /**
     * 自行生成信任库truststore不需要把服务端公钥证书导入的jdk中且url中需要指定域名（此方法不适用于指定Host头）
     * 域名解析可以通过域名服务器或者在hosts文件中配置
     *
     * @throws Exception
     */
    @Test
    public void SSLTestCase2() throws Exception {
        /**
         * 1.初始化密钥库
         */
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = getKeyStore(pfxPath, pfxPasswd, "PKCS12");
        keyManagerFactory.init(keyStore, pfxPasswd.toCharArray());

        /**
         * 2.初始化信任库
         */
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustkeyStore = getKeyStore(trustStroreFile, trustStorePwd, "JKS");
        trustManagerFactory.init(trustkeyStore);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1"}, // supportedProtocols ,这里可以根据实际情况设置
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        /**
         * 3.使用httpclient4.5.8发送post请求
         */
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(30000).build();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
//            httpPost.addHeader("Connection", "keep-alive"); // 设置一些header等
//            httpPost.addHeader("Host", "c.p5.ccb.com");
            String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<name>\n" +
                    "    <FILE_NUM>10</FILE_NUM>\n" +
                    "</name>";
//            StringEntity stringEntity = new StringEntity(requestBody, "UTF-8");
            StringEntity stringEntity = new StringEntity(requestBody, ContentType.create("application/xml", "UTF-8"));
            httpPost.setEntity(stringEntity);
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String respBody = EntityUtils.toString(response.getEntity(), "UTF-8");
            EntityUtils.consume(entity);
            System.out.println(respBody);
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 获得KeyStore
     *
     * @param pfxPath
     * @param pfxPasswd
     * @return
     * @throws Exception
     */
    private KeyStore getKeyStore(String pfxPath, String pfxPasswd, String type) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(type);
        InputStream instream = new FileInputStream(new File(pfxPath));
        try {
            keyStore.load(instream, pfxPasswd.toCharArray());
        } finally {
            instream.close();
        }
        return keyStore;
    }
}