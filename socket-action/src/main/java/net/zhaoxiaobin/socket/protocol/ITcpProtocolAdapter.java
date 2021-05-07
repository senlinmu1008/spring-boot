package net.zhaoxiaobin.socket.protocol;

import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;

import java.io.IOException;
import java.net.Socket;

/**
 * @author zhaoxb
 * @date 2021-05-05 8:30 下午
 */
public interface ITcpProtocolAdapter {
    byte[] decoder(Socket socket, SocketChannelConfig socketChannelConfig) throws IOException;
}
