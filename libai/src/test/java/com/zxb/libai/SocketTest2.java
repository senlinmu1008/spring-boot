package com.zxb.libai;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author zhaoxb
 * @date 2020/12/31 1:35 下午
 */
@Slf4j
public class SocketTest2 {
    public static final int LEN = 4;

    @Test
    @SneakyThrows
    public void testClient() {
        @Cleanup Socket socket = new Socket("139.9.127.172", 21220);
        log.info("=====已连接socket服务端=====");
        @Cleanup OutputStream outputStream = socket.getOutputStream();
        String message = "时间:20210107153530000";
        log.info("发送报文:{}", message);

        // 附上报文头即报文整体长度（包括报文头本身）
        byte[] bytes = message.getBytes(CharsetUtil.CHARSET_UTF_8);
        String lenStr = StringUtils.leftPad(bytes.length + LEN + "", LEN, '0');
        outputStream.write(ArrayUtil.addAll(lenStr.getBytes(), bytes));
        outputStream.flush();

        // 响应
        @Cleanup InputStream inputStream = socket.getInputStream();
        String response = IoUtil.read(inputStream, CharsetUtil.CHARSET_UTF_8);
        log.info("响应报文:{}", response);
    }
}