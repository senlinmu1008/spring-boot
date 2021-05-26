/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.parsecsv.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import net.zhaoxiaobin.parsecsv.convert.ConvertToBigDecimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 校验必输项以及做类型转换
 *
 * @author zhaoxb
 * @create 2019-11-14 16:20
 */
@Data
public class CarCsvDTOConvertAndValid {
    @CsvBindByName(column = "id", required = true)
    private String id;

    @CsvBindByName(column = "short_name")
    private String shortName;

    @CsvBindByName(column = "name")
    private String name;

    @CsvCustomBindByName(column = "remark", converter = ConvertToBigDecimal.class)
    private BigDecimal remark;

    @CsvBindByName(column = "parent_id")
    private String parentId;

    @CsvBindByName(column = "type_name")
    private String typeName;

    @CsvBindByName(column = "type_id")
    private String typeId;
}