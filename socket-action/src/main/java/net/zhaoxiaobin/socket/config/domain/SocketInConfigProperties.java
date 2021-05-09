package net.zhaoxiaobin.socket.config.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static net.zhaoxiaobin.socket.common.IListenService.MAX_CONCURRENCY;

/**
 * @author zhaoxb
 * @date 2021-05-05 1:17 下午
 */
//@JacksonXmlRootElement(localName = "socket")
@ConfigurationProperties(prefix = "socket.in")
@Component
public class SocketInConfigProperties {
    private static final Logger logger = LoggerFactory.getLogger(SocketInConfigProperties.class);
    /**
     * 多个渠道的配置信息
     */
//    @JacksonXmlProperty(localName = "channel")
//    @JacksonXmlElementWrapper(useWrapping = false)
    private List<SocketChannelConfig> socketChannelConfigList;

    @PostConstruct
    public void init() {
        socketChannelConfigList.forEach(socketChannelConfig -> {
            if (socketChannelConfig.getMaxConcurrency() <= 0) {
                logger.warn("端口:{}渠道设置的最大并发数:{}不合理,重新设为:{}", socketChannelConfig.getPort(), socketChannelConfig.getMaxConcurrency(), MAX_CONCURRENCY);
                socketChannelConfig.setMaxConcurrency(MAX_CONCURRENCY);
            }
        });
    }

    public List<SocketChannelConfig> getSocketChannelConfigList() {
        return socketChannelConfigList;
    }

    public void setSocketChannelConfigList(List<SocketChannelConfig> socketChannelConfigList) {
        this.socketChannelConfigList = socketChannelConfigList;
    }
}