package com.zxb.libai.xml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zxb.libai.domain.MessageEntity;
import org.junit.Test;

import java.io.IOException;

/**
 * @author zhaoxb
 * @date 2020/08/22 9:35 下午
 */
public class ParseByJackson {
    private String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    @Test
    public void testParseByJackson() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<msg>\n" +
                "  <head>\n" +
                "    <app>testApp</app>\n" +
                "    <version>1.0</version>\n" +
                "    <date>20200822</date>\n" +
                "    <time>213432</time>\n" +
                "    <txnCode>10000</txnCode>\n" +
                "    <seqNo>1297165260670767104</seqNo>\n" +
                "    <reserve>预留字段</reserve>\n" +
                "  </head>\n" +
                "  <body attr=\"2020\">\n" +
                "    <custName>李白</custName>\n" +
                "    <idNo>310000000000000000</idNo>\n" +
                "    <address>上海</address>\n" +
                "    <hobbies>\n" +
                "      <hobby>饮酒</hobby>\n" +
                "      <hobby>写诗</hobby>\n" +
                "      <hobby>旅游</hobby>\n" +
                "    </hobbies>\n" +
                "    <detail>\n" +
                "      <schoolName>丁荷小学</schoolName>\n" +
                "      <district>0571</district>\n" +
                "      <level>A</level>\n" +
                "    </detail>\n" +
                "    <detail>\n" +
                "      <schoolName>采荷中学</schoolName>\n" +
                "      <district>0571</district>\n" +
                "      <level>A</level>\n" +
                "    </detail>\n" +
                "    <detail>\n" +
                "      <schoolName>学军中学</schoolName>\n" +
                "      <district>0791</district>\n" +
                "      <level>A+</level>\n" +
                "    </detail>\n" +
                "  </body>\n" +
                "</msg>\n";

        // 解析xml
        XmlMapper mapper = new XmlMapper();
        /** 如果存在节点没有实体中属性与之对应，解析不报错 */
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MessageEntity messageEntity = mapper.readValue(xml, MessageEntity.class);

        // 再把对象转回xml
        String genXml = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(messageEntity);
        genXml = xmlHead.concat(genXml);
        System.out.println(genXml);
        System.out.println(genXml.equals(xml));
    }
}