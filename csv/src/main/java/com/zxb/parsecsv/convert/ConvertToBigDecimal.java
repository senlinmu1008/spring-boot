/**
 * Copyright (C), 2015-2019
 */
package com.zxb.parsecsv.convert;

import com.opencsv.bean.AbstractBeanField;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 *
 * @author zhaoxb
 * @create 2019-11-23 13:20
 */
public class ConvertToBigDecimal extends AbstractBeanField {
    @Override
    protected Object convert(String value) {
        if(StringUtils.isNotBlank(value)) {
            return new BigDecimal(value);
        }
        return new BigDecimal(0);
    }
}