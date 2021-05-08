package net.zhaoxiaobin.socket.service.impl;

import net.zhaoxiaobin.socket.protocol.ITcpProtocolAdapter;
import net.zhaoxiaobin.socket.utils.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaoxb
 * @date 2021-05-05 8:37 下午
 */
@Service
public class SocketServiceImpl1 implements ITcpProtocolAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SocketServiceImpl1.class);

    @Override
    public byte[] decoder(Socket socket) throws IOException {
        logger.info("==========开始处理socket交易==========");
        // 方式一：读取直到socket关闭
        byte[] inBytes = SocketUtils.readUntilClose(socket);

//        // 方式二：读取到指定字符为止，比如到换行符\n为止，这里填换行符的十六进制形式0A
//        byte[] inBytes = SocketUtils.readUntilEOF(socket, "0A");
//        // 还可以指定读取的超时时间
//        byte[] inBytes = SocketUtils.readUntilEOF(socket, "0A", 5000);
//        // 结束符以字节数组形式传入
//        byte[] inBytes = SocketUtils.readUntilEOF(socket, "\n".getBytes());
//
//        // 方式三：读取固定长度的字节数，头部代表总长度的字节数，总长度是否包含头部字节数
//        byte[] inBytes = SocketUtils.readFixedLen(socket, 4, true);
//        // 还可以指定读取的超时时间
//        byte[] inBytes = SocketUtils.readFixedLen(socket, 4, true, 5000);
        logger.info("接收报文:{}", new String(inBytes, StandardCharsets.UTF_8));
        // 响应
        return "success-交易成功".getBytes(StandardCharsets.UTF_8);
    }
}