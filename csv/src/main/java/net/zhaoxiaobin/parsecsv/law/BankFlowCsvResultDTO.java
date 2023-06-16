package net.zhaoxiaobin.parsecsv.law;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 银行流水统计结果DTO
 *
 * @author zhaoxb
 * @date 2023-06-16 上午11:20
 */
@Data
public class BankFlowCsvResultDTO {
    /**
     * 对手户名
     */
    private String targetAccountName;

    /**
     * 总笔数
     */
    private int txCount;

    /**
     * 总金额
     */
    private BigDecimal totalAmt;
}