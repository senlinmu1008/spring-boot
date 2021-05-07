package net.zhaoxiaobin.socket.common;

import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaoxb
 * @date 2021-05-07 4:48 下午
 */
public interface IListenService {
    /**
     * 默认单个渠道的最大并发数
     */
    int MAX_CONCURRENCY = 100;

    /**
     * 对单个渠道进行最大并发数限制，key-端口，value-当前并发数
     */
    Map<Integer, AtomicInteger> MAX_CONCURRENCY_MAP = new ConcurrentHashMap();

    void startListen(List<SocketChannelConfig> socketChannelConfigList);
}
