package net.zhaoxiaobin.socket.listener;

import net.zhaoxiaobin.socket.config.SocketChannelConfigParse;
import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;
import net.zhaoxiaobin.socket.protocol.TcpProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * 当spring容器将所有的bean都初始化完成后执行该类
 *
 * @author zhaoxb
 * @date 2021-04-27 11:35 上午
 */
//@Component
public class SocketServerListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SocketServerListener.class);

    @Autowired
    private SocketChannelConfigParse socketChannelConfigParse;

    /**
     * 处理socket交易的线程池
     */
    @Autowired
    private Executor socketExecutor;

    /**
     * 在web 项目中（spring mvc），系统会存在两个容器，一个是root application context，另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）。
     * 这种情况下，就会造成onApplicationEvent方法被执行两次。为了避免上面提到的问题，我们可以只在root application context初始化完成后调用逻辑代码，其他的容器的初始化完成，则不做任何处理
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 如果是子容器，则不执行该方法
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        List<SocketChannelConfig> socketChannelConfigList = socketChannelConfigParse.getSocketChannelConfigList();
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