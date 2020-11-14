/**
 * Copyright (C), 2015-2019
 */
package com.zxb.parsecsv.web;

import cn.hutool.core.util.CharsetUtil;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.zxb.parsecsv.domain.CarCsvDTOByMappingArray;
import com.zxb.parsecsv.domain.CarCsvDTOByName;
import com.zxb.parsecsv.domain.CarCsvDTOByPosition;
import com.zxb.parsecsv.domain.CarCsvDTOByTranslate;
import com.zxb.parsecsv.domain.CarCsvDTOConvertAndValid;
import com.zxb.parsecsv.filter.SkipLineFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaoxb
 * @create 2019-11-14 15:52
 */
@RestController
@RequestMapping("/csv")
public class ParseCsvController {

    @PostMapping("/parseByName")
    public List parseByName(MultipartFile file) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(file.getInputStream(), CharsetUtil.CHARSET_GBK);
        // 设置解析策略，csv的头和POJO属性的名称对应，也可以使用@CsvBindByName注解来指定名称
        HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy();
        strategy.setType(CarCsvDTOByName.class);

        CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                .withMappingStrategy(strategy)
                .build();
        List carCsvDTOList = csvToBean.parse();
        return carCsvDTOList;
    }

    @PostMapping("/parseByPosition")
    public List parseByPosition(String filePath) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(new FileInputStream(filePath), CharsetUtil.CHARSET_GBK);
        // 设置解析策略，使用@CsvBindByPosition注解可以指定字段在csv文件头中的位置，从0开始
        ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
        strategy.setType(CarCsvDTOByPosition.class);

        CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                .withMappingStrategy(strategy)
                .build();
        List carCsvDTOList = csvToBean.parse();
        return carCsvDTOList;
    }

    @PostMapping("/parseByMappingArray")
    public List parseByMappingArray(String filePath) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(new FileInputStream(filePath), CharsetUtil.CHARSET_GBK);
        // 设置解析策略，csv文件不需要头，由程序指定
        ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
        strategy.setType(CarCsvDTOByMappingArray.class);
        String headers = "id|shortName|name|remark|parentId|typeName|typeId";
        String[] headerArr = headers.split("\\|");
        strategy.setColumnMapping(headerArr);

        CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                .withMappingStrategy(strategy)
                .build();
        List carCsvDTOList = csvToBean.parse();
        return carCsvDTOList;
    }

    @PostMapping("/parseByMappingByTranslate")
    public List parseByMappingByTranslate(String filePath) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(new FileInputStream(filePath), CharsetUtil.CHARSET_GBK);
        // 设置解析策略，key-csv的头、value-DTO属性
        HeaderColumnNameTranslateMappingStrategy strategy = new HeaderColumnNameTranslateMappingStrategy();
        strategy.setType(CarCsvDTOByTranslate.class);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("id", "id");
        columnMapping.put("short_name", "shortName");
        columnMapping.put("name", "name");
        columnMapping.put("remark", "remark");
        columnMapping.put("parent_id", "parentId");
        columnMapping.put("type_name", "typeName");
        columnMapping.put("type_id", "typeId");
        strategy.setColumnMapping(columnMapping);

        CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                .withMappingStrategy(strategy)
                .build();
        List carCsvDTOList = csvToBean.parse();
        return carCsvDTOList;
    }

    @PostMapping("/convertAndValid")
    public List convertAndValid(String filePath) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(new FileInputStream(filePath), CharsetUtil.CHARSET_GBK);
        HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy();
        strategy.setType(CarCsvDTOConvertAndValid.class);
        // 校验必输项以及做类型转换
        CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                .withMappingStrategy(strategy)
                .build();
        List carCsvDTOList = csvToBean.parse();
        return carCsvDTOList;
    }

    @PostMapping("/parseBySelf")
    public List parseBySelf(String filePath) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(new FileInputStream(filePath), CharsetUtil.CHARSET_GBK);
        HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy();
        strategy.setType(CarCsvDTOConvertAndValid.class);

        CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                .withSkipLines(2) // 跳过行数
                .withSeparator('|') // 分隔符
                .withFilter(new SkipLineFilter())
                .withThrowExceptions(false) // 如果有必输项没有，则不抛异常忽略此行
                .withMappingStrategy(strategy)
                .build();
        List carCsvDTOList = csvToBean.parse();
        return carCsvDTOList;
    }

}