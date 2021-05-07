package net.zhaoxiaobin.socket;

import net.zhaoxiaobin.socket.config.domain.SocketInConfigProperties;
import net.zhaoxiaobin.socket.common.IListenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @author zhaoxb
 * @date 2021-05-05 1:15 下午
 */
//@Component
public class SocketActionApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private SocketInConfigProperties socketInConfigProperties;

    @Autowired
    private IListenService listenService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 开启监听
        listenService.startListen(socketInConfigProperties.getSocketChannelConfigList());
    }
}