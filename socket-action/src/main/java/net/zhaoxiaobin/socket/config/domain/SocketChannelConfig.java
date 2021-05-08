package net.zhaoxiaobin.socket.config.domain;

import static net.zhaoxiaobin.socket.common.IListenService.MAX_CONCURRENCY;

/**
 * @author zhaoxb
 * @date 2021-05-05 1:19 下午
 */
public class SocketChannelConfig {
    /**
     * 每个渠道的本地监听端口
     */
    private int port;

    /**
     * 交易处理的实现类
     */
    private String beanName;

    /**
     * 最大并发数
     */
    private int maxConcurrency = MAX_CONCURRENCY;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public int getMaxConcurrency() {
        return maxConcurrency;
    }

    public void setMaxConcurrency(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }
}