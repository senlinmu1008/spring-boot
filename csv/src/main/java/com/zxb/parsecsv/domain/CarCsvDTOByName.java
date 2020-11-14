/**
 * Copyright (C), 2015-2019
 */
package com.zxb.parsecsv.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * 根据属性名或注解指定名称来映射
 * 只能定义简单类型（字面量直接赋值）
 *
 * @author zhaoxb
 * @create 2019-11-14 16:20
 */
@Data
public class CarCsvDTOByName {
    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "short_name")
    private String shortName;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "remark")
    private String remark;

    @CsvBindByName(column = "parent_id")
    private String parentId;

    @CsvBindByName(column = "type_name")
    private String typeName;

    @CsvBindByName(column = "type_id")
    private String typeId;
}