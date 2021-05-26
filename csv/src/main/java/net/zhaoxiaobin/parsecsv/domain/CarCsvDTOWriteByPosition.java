/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.parsecsv.domain;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 写出csv文件实体类，不带文件头
 *
 * @author zhaoxb
 * @create 2019-11-14 16:20
 */
@Data
@Accessors(chain = true)
public class CarCsvDTOWriteByPosition {
    @CsvBindByPosition(position = 0)
    private String id;

    @CsvBindByPosition(position = 1)
    private String shortName;

    @CsvBindByPosition(position = 2)
    private String name;

    @CsvBindByPosition(position = 3)
    private String remark;

    @CsvBindByPosition(position = 4)
    private String parentId;

    @CsvBindByPosition(position = 5)
    private String typeName;

    @CsvBindByPosition(position = 6)
    private String typeId;
}