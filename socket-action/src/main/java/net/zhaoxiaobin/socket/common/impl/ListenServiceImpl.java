package net.zhaoxiaobin.socket.common.impl;

import net.zhaoxiaobin.socket.common.IListenService;
import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;
import net.zhaoxiaobin.socket.protocol.TcpProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaoxb
 * @date 2021-05-07 4:50 下午
 */
@Service
public class ListenServiceImpl implements IListenService {
    private static final Logger logger = LoggerFactory.getLogger(ListenServiceImpl.class);

    /**
     * 处理socket交易的线程池
     */
    @Autowired
    private Executor socketExecutor;

    @Override
    public void startListen(List<SocketChannelConfig> socketChannelConfigList) {
        if (socketChannelConfigList == null || socketChannelConfigList.isEmpty()) {
            logger.info("没有配置socket交易接入,不起本地监听端口");
            return;
        }
        // 创建监听本地端口的线程池，固定大小等于接入渠道的监听端口总数
        ExecutorService listenThreadPool = Executors.newFixedThreadPool(socketChannelConfigList.size());
        socketChannelConfigList.forEach(socketChannelConfig -> {
            // 初始化每个渠道的并发数，起始为0
            MAX_CONCURRENCY_MAP.put(socketChannelConfig.getPort(), new AtomicInteger());
            listenThreadPool.execute(new Thread(() -> {
                logger.info("==========服务端socket进入监听,端口号:{}==========", socketChannelConfig.getPort());
                try (ServerSocket serverSocket = new ServerSocket(socketChannelConfig.getPort())) {
                    while (!Thread.currentThread().isInterrupted()) {
                        Socket socket = null;
                        try {
                            socket = serverSocket.accept();
                            logger.info("==========本地端口:{},已连接1个客户端:{},开始处理socket交易==========", socketChannelConfig.getPort(), socket.getRemoteSocketAddress());
                            // 限制最大并发数
                            int currentConcurrency = MAX_CONCURRENCY_MAP.get(socketChannelConfig.getPort()).get();
                            // 如果已经达到最大并发数，关闭客户端socket，继续进入监听状态
                            if (currentConcurrency >= socketChannelConfig.getMaxConcurrency()) {
                                logger.warn("端口:{}渠道已达最大并发数:{}", socketChannelConfig.getPort(), socketChannelConfig.getMaxConcurrency());
                                socket.close();
                                continue;
                            }
                            Runnable socketHandler = new TcpProtocolHandler(socket, socketChannelConfig);
                            // 交给线程池去执行
                            socketExecutor.execute(socketHandler);
                            // 接入计数+1，无论是立即执行还是放入线程池的等待队列中，都计入并发数
                            MAX_CONCURRENCY_MAP.get(socketChannelConfig.getPort()).incrementAndGet();
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
            }));
        });
    }
}