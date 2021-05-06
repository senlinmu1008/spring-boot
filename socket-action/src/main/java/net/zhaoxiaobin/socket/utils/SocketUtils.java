package net.zhaoxiaobin.socket.utils;

import cn.hutool.core.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 从socket输入流中读取数据3种方式
 * 1.读取到客户端关闭socket为止
 * 2.读取到结束符(字节)EOF为止
 * 3.读取固定长度的字节数组，头部含有总长度，总长度可能包含头部字节，也可能不包含
 * <p>
 * 客户端socket和输入流不可在此类中关闭
 *
 * @author zhaoxb
 * @date 2021-04-27 2:37 下午
 */
public class SocketUtils {
    private static final Logger logger = LoggerFactory.getLogger(SocketUtils.class);

    /**
     * 缓冲区大小
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * 默认超时时间
     */
    public static final int DEFAULT_READ_TIMEOUT = 5000;


    /**
     * 从socket输入流中读取字节数组直到关闭结束
     *
     * @param socket 客户端socket
     * @return
     * @throws IOException
     */
    public static byte[] readUntilClose(Socket socket) throws IOException {
        return StreamUtils.copyToByteArray(socket.getInputStream());
    }

    /**
     * 从socket输入流中读取字节数组直到读取到结尾字符
     *
     * @param socket 客户端socket
     * @param endHex 结尾字符十六进制形式
     * @return
     * @throws IOException
     */
    public static byte[] readUntilEOF(Socket socket, String endHex) throws IOException {
        return readUntilEOF(socket, endHex, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 从socket输入流中读取字节数组直到读取到结尾字符
     *
     * @param socket      客户端socket
     * @param endHex      结尾字符十六进制形式
     * @param readTimeout 读取超时时间，单位：毫秒 默认5秒
     * @return
     * @throws IOException
     */
    public static byte[] readUntilEOF(Socket socket, String endHex, int readTimeout) throws IOException {
        // 判断结束符的有效性
        if (endHex == null || endHex.length() < 2) {
            logger.error("结束符需要为有效的十六进制字符");
            throw new RuntimeException("结束符需要为有效的十六进制字符");
        }
        byte[] endBytes = HexUtil.decodeHex(endHex);
        // 开始读取数据，到结束符为止
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        long startTime = System.currentTimeMillis();
        do {
            if (inputStream.available() <= 0) {
                continue;
            }
            int length = inputStream.read(buffer);
            // 写入缓冲区
            byteArrayOutputStream.write(buffer, 0, length);
            if (byteArrayOutputStream.size() < endBytes.length) {
                continue;
            }
            // 判断是否结束，这里不考虑如果对端socket在结束符之后还有数据被我们读取到，即不进行回溯
            byte[] allBytes = byteArrayOutputStream.toByteArray();
            byte[] subBytes = Arrays.copyOfRange(allBytes, allBytes.length - endBytes.length, allBytes.length);
            if (!Arrays.equals(subBytes, endBytes)) {
                // 还未结束，继续读
                continue;
            }
            return Arrays.copyOfRange(allBytes, 0, allBytes.length - endBytes.length);
        } while ((System.currentTimeMillis() - startTime) < readTimeout);
        logger.error("读取socket数据超时,未读取到结束符:{}", endHex.toUpperCase());
        throw new RuntimeException("读取socket数据超时");
    }

    /**
     * 从socket输入流中读取固定长度的字节
     *
     * @param socket  客户端socket
     * @param len     代表总长度的字节数
     * @param contain 总长度是否包含代表长度的字节
     * @return
     * @throws IOException
     */
    public static byte[] readFixedLen(Socket socket, int len, boolean contain) throws IOException {
        return readFixedLen(socket, len, contain, DEFAULT_READ_TIMEOUT);
    }

    /**
     * 从socket输入流中读取固定长度的字节
     *
     * @param socket      客户端socket
     * @param len         代表总长度的字节数
     * @param contain     总长度是否包含代表长度的字节
     * @param readTimeout 读取超时时间，单位：毫秒 默认5秒
     * @return
     * @throws IOException
     */
    public static byte[] readFixedLen(Socket socket, int len, boolean contain, int readTimeout) throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        long startTime = System.currentTimeMillis();
        // 总长度
        int needToRead = -1;
        // 开始循环读取数据
        do {
            if (inputStream.available() <= 0) {
                continue;
            }
            int length = inputStream.read(buffer);
            // 写入缓冲区
            byteArrayOutputStream.write(buffer, 0, length);
            if (byteArrayOutputStream.size() < len) {
                continue;
            }
            // 解析报文总长度
            if (needToRead < 0) {
                byte[] lenBytes = Arrays.copyOfRange(byteArrayOutputStream.toByteArray(), 0, len);
                String lenStr = new String(lenBytes, Charset.defaultCharset()).trim();
                logger.debug("报文总长度:{}", lenStr);
                int totalLen = Integer.parseInt(lenStr);
                needToRead = contain ? totalLen - len : totalLen;
                // 判断长度是否有意义
                if (needToRead <= 0) {
                    return new byte[0];
                }
            }
            // 判断缓冲区大小是否已经读取到指定长度
            if (byteArrayOutputStream.size() - len >= needToRead) {
                byte[] allBytes = byteArrayOutputStream.toByteArray();
                if (allBytes.length - len > needToRead) {
                    logger.warn("对端socket实际发送的数据超过指定长度");
                }
                return Arrays.copyOfRange(allBytes, len, needToRead + len);
            }
        } while ((System.currentTimeMillis() - startTime) < readTimeout);
        logger.error("读取socket数据超时,未读取到完整数据,实际读取字节长度:{}", byteArrayOutputStream.size());
        throw new RuntimeException("读取socket数据超时");
    }
}