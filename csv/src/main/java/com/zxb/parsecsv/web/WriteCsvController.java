/**
 * Copyright (C), 2015-2019
 */
package com.zxb.parsecsv.web;

import cn.hutool.core.util.CharsetUtil;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.zxb.parsecsv.domain.CarCsvDTOWriteByName;
import com.zxb.parsecsv.domain.CarCsvDTOWriteByPosition;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author zhaoxb
 * @create 2019-11-23 20:57
 */
@RestController
@Slf4j
@RequestMapping("/csv")
public class WriteCsvController {

    /**
     * 通过字段数组的集合写出csv文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @PostMapping("/writeCsvFile")
    public void writeCsvFile(String filePath) throws IOException {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"id", "short_name", "name", "remark", "parent_id", "type_name", "type_id"});
        list.add(new String[]{"1", "", "大型汽车号牌", "1.00", "", "号牌种类", "1"});
        list.add(new String[]{"2", "", "小型汽车号牌", "2.00", "", "号牌种类", "1"});
        list.add(new String[]{"3", "", "使馆汽车号牌", "3.50", "", "号牌种类", "1"});

        @Cleanup ICSVWriter icsvWriter = new CSVWriterBuilder(new FileWriterWithEncoding(filePath, CharsetUtil.CHARSET_GBK))
                .withSeparator(ICSVWriter.DEFAULT_SEPARATOR) // 分隔符
                .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER) // 不使用引号
                .build();
        icsvWriter.writeAll(list);
    }

    /**
     * 带有文件头，通过字段名映射生成文件头
     *
     * @param filePath 文件路径
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    @PostMapping("/writeCsvFileByName")
    public void writeCsvFileByName(String filePath) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<CarCsvDTOWriteByName> list = new ArrayList<>();
        list.add(new CarCsvDTOWriteByName()
                .setId("1")
                .setName("大型汽车号牌")
                .setRemark("1.00")
                .setTypeName("号牌种类")
                .setTypeId("1"));
        list.add(new CarCsvDTOWriteByName()
                .setId("2")
                .setName("小型汽车号牌")
                .setRemark("2.00")
                .setTypeName("号牌种类")
                .setTypeId("1"));
        list.add(new CarCsvDTOWriteByName()
                .setId("3")
                .setName("使馆汽车号牌")
                .setRemark("3.50")
                .setTypeName("号牌种类")
                .setTypeId("1"));

        HeaderColumnNameMappingStrategy<CarCsvDTOWriteByName> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CarCsvDTOWriteByName.class);

        /*
        定义头字段的顺序
        实体类注解中指定的字段名在下面字符串中的位置大小决定头字段的顺序
        必须大写
         */
        String headers = "ID|SHORT_NAME|NAME|REMARK|PARENT_ID|TYPE_NAME|TYPE_ID";
        strategy.setColumnOrderOnWrite(Comparator.comparingInt(headers::indexOf));

        @Cleanup Writer writer = new FileWriterWithEncoding(filePath, CharsetUtil.CHARSET_GBK);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(ICSVWriter.DEFAULT_SEPARATOR) // 分隔符
                .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER) // 不使用引号
                .withMappingStrategy(strategy) // 映射策略
                .build();
        beanToCsv.write(list);
    }

    /**
     * 不带文件头，指定字段位置
     *
     * @param filePath 文件路径
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    @PostMapping("/writeCsvFileByPosition")
    public void writeCsvFileByPosition(String filePath) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<CarCsvDTOWriteByPosition> list = new ArrayList<>();
        list.add(new CarCsvDTOWriteByPosition()
                .setId("1")
                .setName("大型汽车号牌")
                .setRemark("1.00")
                .setTypeName("号牌种类")
                .setTypeId("1"));
        list.add(new CarCsvDTOWriteByPosition()
                .setId("2")
                .setName("小型汽车号牌")
                .setRemark("2.00")
                .setTypeName("号牌种类")
                .setTypeId("1"));
        list.add(new CarCsvDTOWriteByPosition()
                .setId("3")
                .setName("使馆汽车号牌")
                .setRemark("3.50")
                .setTypeName("号牌种类")
                .setTypeId("1"));

        ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
        strategy.setType(CarCsvDTOWriteByPosition.class);

        @Cleanup Writer writer = new FileWriterWithEncoding(filePath, CharsetUtil.CHARSET_GBK);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(ICSVWriter.DEFAULT_SEPARATOR) // 分隔符
                .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER) // 不使用引号
                .withMappingStrategy(strategy) // 映射策略
                .build();
        beanToCsv.write(list);
    }
}