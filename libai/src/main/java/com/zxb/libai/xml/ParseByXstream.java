package com.zxb.libai.xml;

import com.thoughtworks.xstream.XStream;
import com.zxb.libai.domain.MessageEntity;
import org.junit.Test;

/**
 * @author zhaoxb
 * @date 2020/08/22 9:49 下午
 */
public class ParseByXstream {
    private String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    @Test
    public void testParseByXstream() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<msg>\n" +
                "  <head>\n" +
                "    <app>testApp</app>\n" +
                "    <version>1.0</version>\n" +
                "    <date>20200822</date>\n" +
                "    <time>225004</time>\n" +
                "    <txnCode>10000</txnCode>\n" +
                "    <seqNo>1297184268082089984</seqNo>\n" +
                "    <reserve>预留字段</reserve>\n" +
                "  </head>\n" +
                "  <body attr=\"2020\">\n" +
                "    <custName>李白</custName>\n" +
                "    <idNo>310000000000000000</idNo>\n" +
                "    <address>上海</address>\n" +
                "    <hobby>饮酒</hobby>\n" +
                "    <hobby>写诗</hobby>\n" +
                "    <hobby>旅游</hobby>\n" +
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
                "</msg>";

        // 解析xml
        XStream xStream = new XStream();
        xStream.processAnnotations(MessageEntity.class);
        // 注意如果实体类中没有xml节点的对应属性，则会抛出异常
        MessageEntity messageEntity = (MessageEntity) xStream.fromXML(xml);

        // 再把对象转回xml
        String genXml = xStream.toXML(messageEntity);
        genXml = xmlHead.concat(genXml);
        System.out.println(genXml);
        System.out.println(genXml.equals(xml));
    }
}