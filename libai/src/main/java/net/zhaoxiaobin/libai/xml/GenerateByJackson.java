package net.zhaoxiaobin.libai.xml;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.zhaoxiaobin.libai.domain.BodyEntity;
import net.zhaoxiaobin.libai.domain.DetailEntity;
import net.zhaoxiaobin.libai.domain.HeadEntity;
import net.zhaoxiaobin.libai.domain.MessageEntity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author zhaoxb
 * @date 2020/08/22 8:49 下午
 */
public class GenerateByJackson {
    private String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    @Test
    public void testGenerateByJackson() throws JsonProcessingException {
        MessageEntity messageEntity = new MessageEntity();
        // 报文头
        HeadEntity headEntity = new HeadEntity();
        headEntity.setApp("testApp");
        headEntity.setVersion("1.0");
        headEntity.setDate(DateUtil.format(new Date(), "yyyyMMdd"));
        headEntity.setTime(DateUtil.format(new Date(), "HHmmss"));
        headEntity.setTxnCode("10000");
        headEntity.setSeqNo(new Snowflake(1, 1).nextIdStr());
        headEntity.setReserve("预留字段");
        messageEntity.setHeadEntity(headEntity);
        // 报文体
        BodyEntity bodyEntity = new BodyEntity();
        bodyEntity.setAttr("2020");
        bodyEntity.setCustName("李白");
        bodyEntity.setIdNo("310000000000000000");
        bodyEntity.setAddress("上海");
        // 循环域1
        List<String> hobby = new ArrayList<>(Arrays.asList(new String[]{"饮酒", "写诗", "旅游"}));
        bodyEntity.setHobbies(hobby);
        // 循环域2
        List<DetailEntity> detailEntityList = new ArrayList<>();
        // 学校1
        DetailEntity school1 = new DetailEntity();
        school1.setSchoolName("丁荷小学");
        school1.setDistrict("0571");
        school1.setLevel("A");
        // 学校2
        DetailEntity school2 = new DetailEntity();
        school2.setSchoolName("采荷中学");
        school2.setDistrict("0571");
        school2.setLevel("A");
        // 学校3
        DetailEntity school3 = new DetailEntity();
        school3.setSchoolName("学军中学");
        school3.setDistrict("0791");
        school3.setLevel("A+");
        detailEntityList.add(school1);
        detailEntityList.add(school2);
        detailEntityList.add(school3);
        bodyEntity.setDetails(detailEntityList);
        messageEntity.setBodyEntity(bodyEntity);

        // 对象转xml
        XmlMapper mapper = new XmlMapper();
        String xml = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(messageEntity);
        xml = xmlHead.concat(xml);
        System.out.println(xml);
    }
}