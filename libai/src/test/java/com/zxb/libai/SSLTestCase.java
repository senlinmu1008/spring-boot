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
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * ### =======客户端jre中导入服务端证书========
 * cd $JAVA_HOME/jre/lib/security
 * # 查看证书
 * keytool -list -keystore cacerts -storepass changeit|grep ccb
 * <p>
 * # 删除证书
 * keytool -delete -alias ccb -keystore cacerts -storepass changeit
 *
 * @author zhaoxb
 * @date 2021/01/05 10:19 上午
 */
public class SSLTestCase {
    /**
     * 客户端p12证书路径
     */
    private String pfxPath = "/Users/zhaoxiaobin/Documents/tienon/nginx/cert/hexp/client_hexp.p12";
    /**
     * 客户端p12证书密码
     */
    private String pfxPasswd = "123456";
    private String url = "https://hexp.p5.ccb.com:21000/p5-http-in/filetransfer/P54211001-2001";
//    private String url = "https://hexp.p5.ccb.com:21000/senddata";

    /**
     * 需要提前在jdk中把服务端公钥证书导入且url中需要指定域名（此方法不适用于指定Host头）
     * 域名解析可以通过域名服务器或者在hosts文件中配置
     *
     * @throws Exception
     */
    @Test
    public void SSLTestCase() throws Exception {
        /**
         * 1.加载P12证书
         */
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream instream = new FileInputStream(new File(pfxPath));
        try {
            keyStore.load(instream, pfxPasswd.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, pfxPasswd.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                new String[]{"TLSv1"}, // supportedProtocols ,这里可以根据实际情况设置
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        /**
         * 2.使用httpclient4.5.8发送post请求
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
}