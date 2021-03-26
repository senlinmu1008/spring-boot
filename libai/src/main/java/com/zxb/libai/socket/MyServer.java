package com.zxb.libai.socket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author zhaoxb
 * @date 2020/12/31 11:50 上午
 */
@Component
@Slf4j
public class MyServer {
    @Autowired
    private ServerHandler serverHandler;

    @PostConstruct
    public void startServer() {
        String socketPort = System.getProperty("socketPort");
        if (socketPort != null) {
            new Thread(() -> receiveMessage(Integer.parseInt(socketPort.trim()))).start();
        }
    }

    @SneakyThrows
    private void receiveMessage(int port) {
        log.info("=====socket服务端进入侦听,端口号:[{}]=====", port);
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                log.info("=====1个客户端已连接:[{}]=====", socket.getRemoteSocketAddress());
                serverHandler.handle(socket);
            } catch (Exception e) {
                log.error("socket接收异常!", e);
            }
        }
    }
}

@Component
@Slf4j
class ServerHandler {
    public static final byte[] EMPTY_ARRAY = new byte[0];

    public static final int LEN = 4;

    @Async
    @SneakyThrows
    public void handle(Socket socket) {
        log.info("=====开始异步读取socket报文=====");
        @Cleanup InputStream inputStream = socket.getInputStream();
        byte[] bytes = this.getBytesFromInputStream(inputStream, LEN);
        String message = new String(bytes, CharsetUtil.CHARSET_UTF_8);
        log.info("socket收到报文:{}", message);

        Integer time = Integer.parseInt(message.substring(message.length() - 2));
        if (time > 0 && time < 60) {
            Thread.sleep(time * 1000);
        }

        // 响应
        @Cleanup OutputStream outputStream = socket.getOutputStream();
        String response = "时间:" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        outputStream.write(response.getBytes(CharsetUtil.CHARSET_UTF_8));
        outputStream.flush();
    }

    /**
     * 客户端不断开连接，报文头指定报文整体长度（包括报文头）的socket数据流读取
     *
     * @param inputStream 输入流
     * @param len         报文头代表整个报文长度的字节数
     * @return
     */
    @SneakyThrows
    private byte[] getBytesFromInputStream(InputStream inputStream, int len) {
        // 解析报文总长度
        byte[] lenBytes = new byte[len];
        int readLen = inputStream.read(lenBytes);
        if (readLen <= 0) {
            log.error("没有读取到任何数据");
            return EMPTY_ARRAY;
        }
        String lenStr = new String(lenBytes, CharsetUtil.CHARSET_UTF_8).trim();
        int totalLen;
        try {
            totalLen = Integer.parseInt(lenStr);
        } catch (NumberFormatException e) {
            log.error("解析报文总长度错误,实际读取表示报文长度的字节数:[{}],报文长度:[{}]", readLen, lenStr, e);
            return EMPTY_ARRAY;
        }
        // 读取数据
        long startReadTime = System.currentTimeMillis();
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        do {
            int length = inputStream.read(buffer);
            if (length == -1) {
                // 断开就结束读取
                return ArrayUtil.addAll(lenBytes, byteArrayOutputStream.toByteArray());
            }
            // 写入缓冲区
            byteArrayOutputStream.write(buffer, 0, length);
            if (byteArrayOutputStream.size() < totalLen - len) {
                // 判断缓冲区大小是否已经读取到指定长度
                continue;
            }
            return ArrayUtil.addAll(lenBytes, byteArrayOutputStream.toByteArray());
        } while ((System.currentTimeMillis() - startReadTime) < 5000);
        log.error("读取TCP数据流超时,未读取到完整的数据,要求:[{}],实际:[{}]!", totalLen - len, byteArrayOutputStream.size());
        return EMPTY_ARRAY;
    }
}