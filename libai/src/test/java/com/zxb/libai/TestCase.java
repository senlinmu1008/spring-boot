/**
 * Copyright (C), 2015-2020
 */
package com.zxb.libai;

import com.zxb.libai.utils.SFTPUtil;
import lombok.SneakyThrows;
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
    @SneakyThrows
    public void test() {
    }
}