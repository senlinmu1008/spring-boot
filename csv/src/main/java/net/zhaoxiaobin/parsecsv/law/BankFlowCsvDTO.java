package net.zhaoxiaobin.parsecsv.law;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

/**
 * 银行流水DTO
 *
 * @author zhaoxb
 * @date 2023-06-16 上午11:25
 */
@Data
public class BankFlowCsvDTO {
    /**
     * 交易卡号
     */
    @CsvBindByPosition(position = 0)
    private String cardNo;

    /**
     * 交易账号
     */
    @CsvBindByPosition(position = 1)
    private String accountNo;

    /**
     * 交易时间，yyyy-MM-dd HH:mm:ss
     */
    @CsvBindByPosition(position = 2)
    private String txTime;

    /**
     * 交易金额
     */
    @CsvBindByPosition(position = 3)
    private String txAmt;

    /**
     * 交易余额
     */
    @CsvBindByPosition(position = 4)
    private String txBal;

    /**
     * 收付标志，进/出
     */
    @CsvBindByPosition(position = 5)
    private String flag;

    /**
     * 交易对手账卡号
     */
    @CsvBindByPosition(position = 6)
    private String targetAccountNo;

    /**
     * 现金标志
     */
    @CsvBindByPosition(position = 7)
    private String cashFlag;

    /**
     * 对手户名
     */
    @CsvBindByPosition(position = 8)
    private String targetAccountName;

    /**
     * 对手身份证号
     */
    @CsvBindByPosition(position = 9)
    private String targetIdNo;

    /**
     * 对手开户银行
     */
    @CsvBindByPosition(position = 10)
    private String openBankName;

    /**
     * 摘要说明
     */
    @CsvBindByPosition(position = 11)
    private String remark;

    /**
     * 交易币种
     */
    @CsvBindByPosition(position = 12)
    private String curType;

    /**
     * 交易网点名称
     */
    @CsvBindByPosition(position = 13)
    private String txOrgName;

    /**
     * 交易发生地
     */
    @CsvBindByPosition(position = 14)
    private String txAdress;

    /**
     * 交易是否成功
     */
    @CsvBindByPosition(position = 15)
    private String successFLag;

    /**
     * 传票号
     */
    @CsvBindByPosition(position = 16)
    private String ticketNo;

    /**
     * ip
     */
    @CsvBindByPosition(position = 17)
    private String ip;

    /**
     * mac
     */
    @CsvBindByPosition(position = 18)
    private String mac;

    /**
     * 对手交易余额
     */
    @CsvBindByPosition(position = 19)
    private String targetTxBal;

    /**
     * 交易流水号
     */
    @CsvBindByPosition(position = 20)
    private String txSeqNo;

    /**
     * 日志号
     */
    @CsvBindByPosition(position = 21)
    private String logNo;

    /**
     * 凭证种类
     */
    @CsvBindByPosition(position = 22)
    private String voucherType;

    /**
     * 凭证号
     */
    @CsvBindByPosition(position = 23)
    private String voucherNo;

    /**
     * 交易柜员号
     */
    @CsvBindByPosition(position = 24)
    private String txTellNo;

    /**
     * 备注
     */
    @CsvBindByPosition(position = 25)
    private String txRemark;

    /**
     * 查询反馈结果原因
     */
    @CsvBindByPosition(position = 26)
    private String feedbackReason;
}