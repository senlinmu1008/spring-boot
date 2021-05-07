package net.zhaoxiaobin.socket;

import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;
import net.zhaoxiaobin.socket.config.domain.SocketInConfigProperties;
import net.zhaoxiaobin.socket.protocol.TcpProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author zhaoxb
 * @date 2021-05-05 1:15 下午
 */
@Component
public class SocketActionApplicationRunnerImpl implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(SocketActionApplicationRunnerImpl.class);

    @Autowired
    private SocketInConfigProperties socketInConfigProperties;

    /**
     * 处理socket交易的线程池
     */
    @Autowired
    private Executor socketExecutor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SocketChannelConfig> socketChannelConfigList = socketInConfigProperties.getSocketChannelConfigList();
        if (socketChannelConfigList == null || socketChannelConfigList.isEmpty()) {
            logger.info("没有配置socket交易接入,不起本地监听端口");
            return;
        }
        // 创建监听本地端口的线程池，固定大小等于接入渠道的监听端口总数
        ExecutorService listenThreadPool = Executors.newFixedThreadPool(socketChannelConfigList.size());
        socketChannelConfigList.forEach(socketChannelConfig -> listenThreadPool.execute(new Thread(() -> {
                    logger.info("==========服务端socket进入监听,端口号:{}==========", socketChannelConfig.getPort());
                    try (ServerSocket serverSocket = new ServerSocket(socketChannelConfig.getPort())) {
                        while (!Thread.currentThread().isInterrupted()) {
                            Socket socket = null;
                            try {
                                socket = serverSocket.accept();
                                logger.info("==========本地端口:{},已连接1个客户端:{},开始处理socket交易==========", socketChannelConfig.getPort(), socket.getRemoteSocketAddress());
                                Runnable socketHandler = new TcpProtocolHandler(socket, socketChannelConfig);
                                // 交给线程池去执行
                                socketExecutor.execute(socketHandler);
                            } catch (IOException e) {
                                logger.error("服务端socket:{}接收失败", socketChannelConfig.getPort(), e);
                            } catch (RejectedExecutionException e) {
                                logger.error("线程池处理已达上限,拒绝当前socket:{}交易", socketChannelConfig.getPort(), e);
                                socket.close();
                            }
                        }
                    } catch (IOException e) {
                        logger.error("服务端socket:{}异常", socketChannelConfig.getPort(), e);
                    }
                }))
        );
    }
}