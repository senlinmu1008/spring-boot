package net.zhaoxiaobin.parsecsv.law;

import com.opencsv.bean.CsvToBeanFilter;
import org.apache.commons.lang3.StringUtils;

/**
 * 银行流水过滤器
 *
 * @author zhaoxb
 * @date 2023-06-16 上午11:16
 */
public class BankFlowCsvFilter implements CsvToBeanFilter {
    @Override
    public boolean allowLine(String[] line) {
        return StringUtils.isNotBlank(line[8]);
    }
}