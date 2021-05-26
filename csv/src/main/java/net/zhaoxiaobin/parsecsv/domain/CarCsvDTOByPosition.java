/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.parsecsv.domain;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

/**
 * 通过指定位置来映射，csv文件不需要带有文件头
 * 只能定义简单类型（字面量直接赋值）
 *
 * @author zhaoxb
 * @create 2019-11-14 16:20
 */
@Data
public class CarCsvDTOByPosition {
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