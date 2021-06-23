/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.libai;

import net.zhaoxiaobin.libai.utils.SFTPUtil;
import org.junit.Test;

import java.math.BigDecimal;

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
    public void testBigDecimal() {
        // 1.不允许浮点数构造为BigDecimal
        // 2.整型构造BigDecimal
        System.out.println(new BigDecimal(10).toString()); // 10
        System.out.println(new BigDecimal(10).setScale(2).toString()); // 10.00
        System.out.println(new BigDecimal(10000000000L).toString()); // 10000000000
        System.out.println(new BigDecimal(10000000000L).setScale(2).toString()); // 10000000000.00
        // 3.字符串整型构造BigDecimal，结果与2一致
        System.out.println(new BigDecimal("10").toString()); // 10
        System.out.println(new BigDecimal("10").setScale(2).toString()); // 10.00
        System.out.println(new BigDecimal("10000000000").toString()); // 10000000000
        System.out.println(new BigDecimal("10000000000").setScale(2).toString()); // 10000000000.00
        // 4.字符串小数构造BigDecimal
        System.out.println(new BigDecimal("10.00").toString()); // 10.00
        System.out.println(new BigDecimal("10.00").setScale(2).toString()); // 10.00
        System.out.println(new BigDecimal("10.23").toString()); // 10.23
        System.out.println(new BigDecimal("10.23").setScale(2).toString()); // 10.23
        System.out.println(new BigDecimal("10000000000.00").toString()); // 10000000000.00
        System.out.println(new BigDecimal("10000000000.00").setScale(2).toString()); // 10000000000.00
        System.out.println(new BigDecimal("10000000000.23").toString()); // 10000000000.23
        System.out.println(new BigDecimal("10000000000.23").setScale(2).toString()); // 10000000000.23
        // 调用stripTrailingZeros方法去除小数点后的0
        System.out.println(new BigDecimal("1.00").stripTrailingZeros().toString()); // 1
        System.out.println(new BigDecimal("1.00").stripTrailingZeros().toPlainString()); // 1
        System.out.println(new BigDecimal("10.00").stripTrailingZeros().toString()); // 1E+1
        System.out.println(new BigDecimal("10.00").stripTrailingZeros().toPlainString()); // 10
        System.out.println(new BigDecimal("100.00").stripTrailingZeros().toString()); // 1E+2
        System.out.println(new BigDecimal("100.00").stripTrailingZeros().toPlainString()); // 100
        System.out.println(new BigDecimal("10.23").stripTrailingZeros().toString()); // 10.23
        System.out.println(new BigDecimal("10.23").stripTrailingZeros().toPlainString()); // 10.23
        System.out.println(new BigDecimal("10000000000.23").stripTrailingZeros().toString()); // 10000000000.23
        System.out.println(new BigDecimal("10000000000.23").stripTrailingZeros().toPlainString()); // 10000000000.23
    }
}