package net.zhaoxiaobin.socket.service.impl;

import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;
import net.zhaoxiaobin.socket.protocol.IProtocolDecoderAdapter;
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
public class ProtocolDecoderAdapterImpl1 implements IProtocolDecoderAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolDecoderAdapterImpl1.class);

    @Override
    public byte[] decoder(Socket socket, SocketChannelConfig socketChannelConfig) throws IOException {
        logger.info("==========开始处理socket交易==========");
        byte[] bytes = SocketUtils.readUntilClose(socket);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        return "success-交易成功".getBytes(StandardCharsets.UTF_8);
    }
}