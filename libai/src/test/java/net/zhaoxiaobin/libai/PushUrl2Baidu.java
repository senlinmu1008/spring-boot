package net.zhaoxiaobin.libai;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.junit.Test;

import java.util.List;

/**
 * @author zhaoxb
 * @date 2022-03-24 下午3:42
 */
public class PushUrl2Baidu {
    private String[] sourceUrls = {
            "https://zhaoxiaobin.net/sitemap.xml",
            "https://ningbolaw.com/sitemap.xml",
            "https://jianglihua.com/sitemap.xml",
    };

    @Test
    public void push() {
        for (String sourceUrl : sourceUrls) {
            try {
                this.pushSiteUrl(sourceUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pushSiteUrl(String sourceUrl) {
        System.out.println("开始推送站点:" + sourceUrl);
        // 获取sitemap
        HttpResponse sourceResponse = HttpRequest.get(sourceUrl).timeout(30000).execute();
        if (!sourceResponse.isOk()) {
            throw new RuntimeException("获取sitemap文件失败:\n" + sourceResponse.body());
        }
        // 解析url集合
        String sitemapXml = sourceResponse.body();
        List<String> urlList = ReUtil.findAll("<loc>(.*?)</loc>", sitemapXml, 1);
        StringBuilder sb = new StringBuilder();
        for (String url : urlList) {
            sb.append(url + "\n");
        }
        String pushUrlBody = sb.toString().trim();
        // 提交url给百度收录
        String domain = sourceUrl.substring(sourceUrl.indexOf("/") + 2, sourceUrl.lastIndexOf("/"));
        String targetUrl = "http://data.zz.baidu.com/urls?site=" + domain + "&token=zooZYfaF1ZxgPuZN";
        HttpResponse targetResponse = HttpRequest.post(targetUrl)
                .header("User-Agent", "curl/7.12.1")
                .header("Host", "data.zz.baidu.com")
                .contentType(ContentType.TEXT_PLAIN.toString())
                .contentLength(pushUrlBody.getBytes().length)
                .body(pushUrlBody)
                .timeout(30000)
                .execute();
        if (!targetResponse.isOk()) {
            throw new RuntimeException("推送url给百度失败:\n" + targetResponse.body());
        }
        System.out.println(targetResponse.body());
    }

}