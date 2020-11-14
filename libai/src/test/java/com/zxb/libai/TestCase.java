/**
 * Copyright (C), 2015-2020
 */
package com.zxb.libai;

import cn.hutool.core.util.ReUtil;
import com.zxb.libai.utils.SFTPUtil;
import jodd.io.FileUtil;
import jodd.util.Base64;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author zhaoxb
 * @create 2020-01-07 12:31
 */
public class TestCase {
    @Test
    @SneakyThrows
    public void pdf() {
        File dir = new File("/Users/zhaoxiaobin/Desktop/log");
        File[] files = dir.listFiles();
        for (File file : files) {
            System.out.println(file);
            String content = FileUtil.readString(file);
            String regex = "\"data\":\"(.*?)\",\"resp";
            ArrayList<String> list = ReUtil.findAll(regex, content, 1, new ArrayList<>());
            for (String s : list) {
                if (s.startsWith("JVBERi")) {
                    FileUtil.writeBytes(new File("/Users/zhaoxiaobin/Desktop/pdf/".concat(UUID.randomUUID().toString()).concat(".pdf")), Base64.decode(s));
                }
            }
        }
    }

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
}