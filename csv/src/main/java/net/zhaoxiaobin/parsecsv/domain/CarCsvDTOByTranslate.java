/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.parsecsv.domain;

import lombok.Data;

/**
 * 自定义csv头和属性名的映射关系
 * 只能定义简单类型（字面量直接赋值）
 *
 * @author zhaoxb
 * @create 2019-11-14 16:20
 */
@Data
public class CarCsvDTOByTranslate {
    private String id;

    private String shortName;

    private String name;

    private String remark;

    private String parentId;

    private String typeName;

    private String typeId;
}