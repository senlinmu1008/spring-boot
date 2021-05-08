package net.zhaoxiaobin.socket.protocol;

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
     * @return
     * @throws IOException
     */
    byte[] decoder(Socket socket) throws IOException;
}
