/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.libai;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import net.zhaoxiaobin.libai.utils.SFTPUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhaoxb
 * @create 2020-01-07 12:31
 */
public class TestCase {
    @Test
    public void testSFTP() {
        SFTPUtil ftp = new SFTPUtil("172.16.122.104", "root", "zxb123");
        ftp.connect();

        String localPath = "/users/zhaoxiaobin/desktop/"; // 本地路径
        String remotepath = "/home/redis/"; // 对端路径
        String fileName = "redis-5.0.8.tar.gz"; // 文件名
        ftp.downloadFile(remotepath, fileName, localPath, fileName);

        ftp.disconnect();
    }

    @Test
    public void earlyRepayment() {
        String remainAmt = "1312524.42";
        int remainPeriod = (int) DateUtil.betweenMonth(new Date(), DateUtil.parse("20500901", "yyyyMMdd"), false);
        // int remainPeriod = 317;
        System.out.println(remainPeriod);
        String interestRate = "3.95";
        BigDecimal monthlyInterestRate = new BigDecimal(interestRate).divide(new BigDecimal(1200), 20, RoundingMode.HALF_UP);
        BigDecimal numerator = new BigDecimal(remainAmt).multiply(monthlyInterestRate).multiply(monthlyInterestRate.add(new BigDecimal(1)).pow(remainPeriod));
        BigDecimal denominator = monthlyInterestRate.add(new BigDecimal(1)).pow(remainPeriod).subtract(new BigDecimal(1));
        System.out.println(numerator.divide(denominator, 20, RoundingMode.HALF_UP).toString());
    }

    @Test
    public void select() {
        String allFilePath = "/Users/zhaoxiaobin/Desktop/Java 8 Gu/全量题.txt";
        String selectedFilePath = "/Users/zhaoxiaobin/Desktop/Java 8 Gu/已答题.txt";
        Set<String> allTopicSet = this.readFile(allFilePath);
        Set<String> selectedTopicSet = this.readFile(selectedFilePath);
        // 取差集
        List<String> subtractList = CollectionUtil.subtractToList(allTopicSet, selectedTopicSet);
        if (subtractList.isEmpty()) {
            System.out.println("题做完了！");
            return;
        }
        List<String> randomTopicList = new ArrayList<>();
        if (subtractList.size() <= 15) {
            // 少于15题，全部输出
            randomTopicList.addAll(subtractList);
        } else {
            // 随机抽15题
            for (int i = 0; i < 15; i++) {
                int randomIndex = RandomUtil.randomInt(subtractList.size());
                randomTopicList.add(subtractList.remove(randomIndex));
            }
        }
        // 写出到本地
        FileUtil.writeLines(randomTopicList, selectedFilePath, "UTF-8", true);
        String timestamp = DateUtil.format(new Date(), "yyyyMMdd HHmmss");
        FileUtil.writeUtf8Lines(randomTopicList, "/Users/zhaoxiaobin/Desktop/Java 8 Gu/" + timestamp + ".txt");
    }

    private Set<String> readFile(String filePath) {
        List<String> topicList = FileUtil.readUtf8Lines(filePath);
        topicList = topicList.parallelStream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return new HashSet<>(topicList);
    }

}