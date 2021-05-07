package net.zhaoxiaobin.socket.config.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static net.zhaoxiaobin.socket.common.IListenService.MAX_CONCURRENT;
import static net.zhaoxiaobin.socket.utils.SocketUtils.DEFAULT_READ_TIMEOUT;

/**
 * @author zhaoxb
 * @date 2021-05-05 1:17 下午
 */
@ConfigurationProperties(prefix = "socket.in")
@JacksonXmlRootElement(localName = "socket")
@Component
public class SocketInConfigProperties {
    /**
     * 多个渠道的配置信息
     */
    @JacksonXmlProperty(localName = "channel")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<SocketChannelConfig> socketChannelConfigList;

    @PostConstruct
    public void init() {
        socketChannelConfigList.forEach(socketChannelConfig -> {
            if (socketChannelConfig.getReadTimeout() < DEFAULT_READ_TIMEOUT) {
                socketChannelConfig.setReadTimeout(DEFAULT_READ_TIMEOUT);
            }
            if (socketChannelConfig.getMaxConcurrency() <= 0) {
                socketChannelConfig.setMaxConcurrency(MAX_CONCURRENT);
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