package net.zhaoxiaobin.socket.protocol;

import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;

import java.io.IOException;
import java.net.Socket;

/**
 * @author zhaoxb
 * @date 2021-05-05 8:30 下午
 */
public interface ITcpProtocolAdapter {
    /**
     * socket接入解码器
     * @param socket 客户端socket
     * @param socketChannelConfig 当前渠道socket配置
     * @return
     * @throws IOException
     */
    byte[] decoder(Socket socket, SocketChannelConfig socketChannelConfig) throws IOException;
}
