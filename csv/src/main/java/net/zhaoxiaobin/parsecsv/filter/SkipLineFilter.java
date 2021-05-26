/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.parsecsv.filter;

import com.opencsv.bean.CsvToBeanFilter;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author zhaoxb
 * @create 2019-11-23 13:54
 */
public class SkipLineFilter implements CsvToBeanFilter {
    @Override
    public boolean allowLine(String[] line) {
        // 首列为空的行过滤掉
        return StringUtils.isNotBlank(line[0]);
    }
}