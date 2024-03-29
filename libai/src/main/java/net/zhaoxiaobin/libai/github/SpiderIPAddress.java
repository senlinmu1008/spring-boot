package net.zhaoxiaobin.libai.github;

import cn.hutool.core.util.ReUtil;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.List;

/**
 * @author zhaoxb
 * @date 2020/10/28 12:27 下午
 */
public class SpiderIPAddress {
    public static final String URL = "https://www.ipaddress.com/ip-lookup";

    public static final String[] ADDRESS = {
            "github.com",
            "gist.github.com",
            "github.global.ssl.fastly.net",
            "raw.githubusercontent.com",
            "assets-cdn.github.com",
            "avatars.githubusercontent.com",
            "avatars0.githubusercontent.com",
            "avatars1.githubusercontent.com",
            "avatars2.githubusercontent.com",
            "avatars3.githubusercontent.com",
            "camo.githubusercontent.com",
            "stackoverflow.com",
    };

    public static final String LOOKUP_REGEX = "<h1>IP Lookup : (.*?) \\(";

    public static final String FORM_REGEX = "type=\"radio\" value=\"(.*?)\"/>";

    public static void main(String[] args) {
        for (String address : ADDRESS) {
            List<String> ipList;
            do {
                ipList = getIPAddress(address);
            } while (ipList.isEmpty());
            for (String ip : ipList) {
                System.out.println(ip + " " + address);
            }
        }
    }

    private static List<String> getIPAddress(String address) {
//        HttpResponse response = HttpRequest.post(URL).form("host", address).timeout(60000).execute();
        HttpResponse response = HttpRequest.post(URL).form("host", address).timeout(60000).send();
        // 先找列表
        String responseBody = response.body();
        List<String> ipList = ReUtil. findAllGroup1(FORM_REGEX, responseBody);
        if (ipList != null && !ipList.isEmpty()) {
            return ipList;
        }
        // 找单个
        List<String> singleIp = ReUtil.findAllGroup1(LOOKUP_REGEX, responseBody);
        return singleIp;
    }
}