package net.zhaoxiaobin.socket;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.zhaoxiaobin.socket.config.domain.SocketChannelConfig;
import net.zhaoxiaobin.socket.config.domain.SocketInConfigProperties;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author zhaoxb
 * @date 2021-05-07 9:43 上午
 */
public class ParseXmlTests {
    private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<socket>\n" +
            "    <channel>\n" +
            "        <port>49394</port>\n" +
            "        <beanName>socketServiceImpl3</beanName>\n" +
            "        <readTimeout>15000</readTimeout>\n" +
            "    </channel>\n" +
            "    <channel>\n" +
            "        <port>59394</port>\n" +
            "        <beanName>socketServiceImpl4</beanName>\n" +
            "    </channel>\n" +
            "</socket>";

    @Test
    public void parse2Obj() throws IOException {
        XmlMapper mapper = new XmlMapper();
        // 如果xml中有节点，但实体类中没有属性对应，不报错
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SocketInConfigProperties socketInConfigProperties = mapper.readValue(xml, SocketInConfigProperties.class);
        List<SocketChannelConfig> socketChannelConfigList = socketInConfigProperties.getSocketChannelConfigList();
        for (SocketChannelConfig socketChannelConfig : socketChannelConfigList) {
            System.out.println(JSONUtil.toJsonPrettyStr(socketChannelConfig));
        }
    }
}