/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.parsecsv.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 写出csv文件实体类，带有文件头
 *
 * @author zhaoxb
 * @create 2019-11-14 16:20
 */
@Data
@Accessors(chain = true)
public class CarCsvDTOWriteByName {
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