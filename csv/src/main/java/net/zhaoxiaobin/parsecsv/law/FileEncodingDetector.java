package net.zhaoxiaobin.parsecsv.law;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件字符集判断
 *
 * @author zhaoxb
 * @date 2023-06-08 下午7:48
 */
@Slf4j
public class FileEncodingDetector {
    /**
     * 文件字符集检测，取前4个字节，不一定百分比准确
     *
     * @param filePath 文件路径
     * @return UTF-8 or GBK
     */
    public static String detectEncoding(String filePath) {
        InputStream fis = null;
        byte[] bytes = new byte[4096];
        try {
            fis = new FileInputStream(filePath);
            int bytesRead = fis.read(bytes);
            return isUTF8(bytes, bytesRead) ? "UTF-8" : "GBK";
        } catch (IOException e) {
            log.error("检测文件:{}字符集异常", filePath, e);
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(fis);
        }
    }

    /**
     * 判断是否为UTF-8
     *
     * @param bytes  检测字节数组
     * @param length 检测长度
     * @return
     */
    private static boolean isUTF8(byte[] bytes, int length) {
        int i = 0;
        int n;

        while (i < length) {
            int c = bytes[i++] & 0xFF;

            if (c <= 0x7F) {
                n = 0;
            } else if ((c & 0xE0) == 0xC0) {
                n = 1;
            } else if ((c & 0xF0) == 0xE0) {
                n = 2;
            } else if ((c & 0xF8) == 0xF0) {
                n = 3;
            } else {
                return false;
            }

            for (int j = 0; j < n; j++) {
                if (i >= length) {
                    return false;
                }
                c = bytes[i++] & 0xFF;
                if ((c & 0xC0) != 0x80) {
                    return false;
                }
            }
        }
        return true;
    }
}
