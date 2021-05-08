package net.zhaoxiaobin.socket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.Cleanup;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.IntStream;

/**
 * @author zhaoxb
 * @date 2021-05-05 10:36 上午
 */
public class SocketTests {
    private static final Logger logger = LoggerFactory.getLogger(SocketTests.class);

    @Test
    public void testReadUntilClose() throws IOException {
        // 建立连接
        @Cleanup Socket socket = new Socket("127.0.0.1", 29394);
        logger.info("=====已连接socket服务端=====");
        // 发送报文
        OutputStream outputStream = socket.getOutputStream();
        String message = "时间:" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        logger.info("发送报文:{}", message);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes);
        outputStream.flush();
        socket.shutdownOutput();
        // 获取响应
        InputStream inputStream = socket.getInputStream();
        String response = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        logger.info("响应报文:{}", response);
    }

    @Test
    public void testReadUntilEOF() throws IOException {
        // 建立连接
        @Cleanup Socket socket = new Socket("127.0.0.1", 29394);
        logger.info("=====已连接socket服务端=====");
        // 发送报文
        OutputStream outputStream = socket.getOutputStream();
        String message = "时间:" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + "\n";
        logger.info("发送报文:{}", message);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes);
        outputStream.flush();
        // 获取响应
        InputStream inputStream = socket.getInputStream();
        String response = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        logger.info("响应报文:{}", response);
    }

    @Test
    public void testReadFixedLen1() throws IOException {
        // 建立连接
        @Cleanup Socket socket = new Socket("127.0.0.1", 29394);
        logger.info("=====已连接socket服务端=====");
        // 发送报文
        OutputStream outputStream = socket.getOutputStream();
        String message = "时间:" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        logger.info("发送报文:{}", message);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        String lenStr = StringUtils.leftPad(bytes.length + 4 + "", 4, '0');
        outputStream.write(ArrayUtils.addAll(lenStr.getBytes(StandardCharsets.UTF_8), bytes));
        outputStream.flush();
        // 获取响应
        InputStream inputStream = socket.getInputStream();
        String response = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        logger.info("响应报文:{}", response);
    }

    @Test
    public void testReadFixedLen2() throws IOException {
        // 建立连接
        @Cleanup Socket socket = new Socket("127.0.0.1", 29394);
        logger.info("=====已连接socket服务端=====");
        // 发送报文
        OutputStream outputStream = socket.getOutputStream();
        String message = "时间:" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        logger.info("发送报文:{}", message);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        String lenStr = StringUtils.leftPad(bytes.length + "", 4, '0');
        outputStream.write(ArrayUtils.addAll(lenStr.getBytes(StandardCharsets.UTF_8), bytes));
        outputStream.flush();
        // 获取响应
        InputStream inputStream = socket.getInputStream();
        String response = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        logger.info("响应报文:{}", response);
    }

    @Test
    public void testConcurrency() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4");
        IntStream.range(Integer.MIN_VALUE, Integer.MAX_VALUE).parallel().forEach(i -> {
            try {
                this.testReadUntilClose();
            } catch (IOException e) {
                logger.error("异常", e);
            }
            ThreadUtil.sleep(1000L);
        });
    }
}