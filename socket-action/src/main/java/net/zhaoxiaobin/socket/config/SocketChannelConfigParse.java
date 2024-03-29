package net.zhaoxiaobin.socket.config;

import cn.hutool.core.io.IoUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;
import net.zhaoxiaobin.socket.config.domain.SocketInConfigProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static net.zhaoxiaobin.socket.common.IListenService.MAX_CONCURRENCY;

/**
 * @author zhaoxb
 * @date 2021-05-06 7:12 下午
 */
public class SocketChannelConfigParse implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SocketChannelConfigParse.class);

    /**
     * socket接入渠道配置文件列表
     */
    private Resource[] configurations;

    /**
     * 多个渠道的配置信息
     */
    private List<SocketChannelConfig> socketChannelConfigList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("开始解析socket接入渠道配置文件");
        for (Resource resource : configurations) {
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
                String xmlContent = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                if (StringUtils.isBlank(xmlContent)) {
                    continue;
                }
                XmlMapper mapper = new XmlMapper();
                // 如果xml中有节点，但实体类中没有属性对应，不报错处理
                // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                List<SocketChannelConfig> socketChannelConfigList = mapper.readValue(xmlContent, SocketInConfigProperties.class).getSocketChannelConfigList();
                if (socketChannelConfigList == null) {
                    continue;
                }
                // 校验必填字段
                socketChannelConfigList.forEach(socketChannelConfig -> {
                    if (socketChannelConfig.getPort() == 0) {
                        logger.error("配置文件:{}接入渠道端口没有配置", resource.getFilename());
                        throw new RuntimeException("配置文件:" + resource.getFilename() + "接入渠道端口没有配置");
                    }
                    if (StringUtils.isBlank(socketChannelConfig.getBeanName())) {
                        logger.error("配置文件:{}接入渠道beanName没有配置", resource.getFilename());
                        throw new RuntimeException("配置文件:" + resource.getFilename() + "接入渠道beanName没有配置");
                    }
                    if (socketChannelConfig.getMaxConcurrency() <= 0) {
                        logger.warn("端口:{}渠道设置的最大并发数:{}不合理,重新设为:{}", socketChannelConfig.getPort(), socketChannelConfig.getMaxConcurrency(), MAX_CONCURRENCY);
                        socketChannelConfig.setMaxConcurrency(MAX_CONCURRENCY);
                    }
                });
                this.socketChannelConfigList.addAll(socketChannelConfigList);
            } finally {
                IoUtil.close(inputStream);
            }
        }
    }

    public Resource[] getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Resource[] configurations) {
        this.configurations = configurations;
    }

    public List<SocketChannelConfig> getSocketChannelConfigList() {
        return socketChannelConfigList;
    }

    public void setSocketChannelConfigList(List<SocketChannelConfig> socketChannelConfigList) {
        this.socketChannelConfigList = socketChannelConfigList;
    }
}