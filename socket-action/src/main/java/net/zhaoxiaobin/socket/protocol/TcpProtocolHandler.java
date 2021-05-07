package net.zhaoxiaobin.socket.protocol;

import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;
import net.zhaoxiaobin.socket.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author zhaoxb
 * @date 2021-04-27 2:21 下午
 */
public class TcpProtocolHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TcpProtocolHandler.class);

    /**
     * 客户端socket
     */
    private Socket socket;

    /**
     * socket接入配置
     */
    private SocketChannelConfig socketChannelConfig;

    public TcpProtocolHandler(Socket socket, SocketChannelConfig socketChannelConfig) {
        this.socket = socket;
        this.socketChannelConfig = socketChannelConfig;
    }

    @Override
    public void run() {
        // 获取处理业务的bean
        String beanName = socketChannelConfig.getBeanName();
        ITcpProtocolAdapter tcpProtocolAdapter = SpringUtils.getBean(beanName, ITcpProtocolAdapter.class);
        try (Socket socket = this.socket) {
            // 处理业务
            byte[] resultBytes = tcpProtocolAdapter.decoder(socket, socketChannelConfig);
            // 返回
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(resultBytes);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("处理socket交易异常", e);
            throw new RuntimeException("处理socket交易异常");
        }
    }
}