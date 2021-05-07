package net.zhaoxiaobin.socket.config.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<SocketChannelConfig> getSocketChannelConfigList() {
        return socketChannelConfigList;
    }

    public void setSocketChannelConfigList(List<SocketChannelConfig> socketChannelConfigList) {
        this.socketChannelConfigList = socketChannelConfigList;
    }
}