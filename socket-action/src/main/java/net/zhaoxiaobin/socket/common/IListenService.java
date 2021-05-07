package net.zhaoxiaobin.socket.common;

import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;

import java.util.List;

/**
 * @author zhaoxb
 * @date 2021-05-07 4:48 下午
 */
public interface IListenService {
    void startListen(List<SocketChannelConfig> socketChannelConfigList);
}
