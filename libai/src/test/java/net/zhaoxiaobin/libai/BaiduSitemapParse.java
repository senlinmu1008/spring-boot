package net.zhaoxiaobin.libai;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;

import java.util.List;

/**
 * @author zhaoxb
 * @date 2022-01-10 下午3:11
 */
public class BaiduSitemapParse {
//    private static String url = "https://ningbolaw.com/baidusitemap.xml";
//    private static String url = "https://jianglihua.com/baidusitemap.xml";
    private static String url = "https://nblawyer.github.io/baidusitemap.xml";

    public static void main(String[] args) {
        String sitemapXml = HttpRequest.get(url).timeout(5000).execute().body();
        List<String> urlList = ReUtil.findAll("<loc>(.*?)</loc>", sitemapXml, 1);
        int cnt = 1;
        for (int i = 0; i < urlList.size(); i++) {
            if (i >0 && i % 20 == 0) {
                System.out.println("==========分割线==========" + cnt);
                cnt++;
                ThreadUtil.sleep(10);
            }
            System.out.println(urlList.get(i));
        }
    }
}