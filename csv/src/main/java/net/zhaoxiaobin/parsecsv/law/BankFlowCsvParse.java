package net.zhaoxiaobin.parsecsv.law;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 银行流水解析
 *
 * @author zhaoxb
 * @date 2023-06-16 下午12:09
 */
@Slf4j
public class BankFlowCsvParse {
    private String filePath = "";

    @Test
    public void parseCsv() {
        // 检测字符集
        String charset = FileEncodingDetector.detectEncoding(filePath);
        InputStreamReader inputStream = null;
        List<BankFlowCsvDTO> bankFlowList;
        try {
            inputStream = new InputStreamReader(new FileInputStream(filePath), charset);
            // 解析csv文件
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(BankFlowCsvDTO.class);
            CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                    .withSkipLines(1) // 跳过行数
                    .withFilter(new BankFlowCsvFilter()) // 过滤符合条件的行
                    .withThrowExceptions(false) // 如果有必输项没有，则不抛异常忽略此行
                    .withMappingStrategy(strategy)
                    .build();
            bankFlowList = csvToBean.parse();
        } catch (IOException e) {
            log.error("解析文件:{}异常", filePath, e);
            return;
        } finally {
            IoUtil.close(inputStream);
        }
        if (bankFlowList.isEmpty()) {
            log.error("文件:{}没有有效数据", filePath);
            return;
        }
        // 将获得的内容list遍历筛选
        Map<String, BankFlowCsvResultDTO> outputMap = new HashMap<>();
        Map<String, BankFlowCsvResultDTO> inputMap = new HashMap<>();
        for (BankFlowCsvDTO bankFlow : bankFlowList) {
            String flag = StringUtils.trim(bankFlow.getFlag());
            if ("出".equals(flag)) {
                this.handle(outputMap, bankFlow);
            } else if ("进".equals(flag)) {
                this.handle(inputMap, bankFlow);
            }
        }
        if (outputMap.isEmpty() && inputMap.isEmpty()) {
            log.error("文件:{}没有数据输出", filePath);
            return;
        }
        // 排序
        Map<String, BankFlowCsvResultDTO> sortedOutputMap = this.sortedMap(outputMap);
        Map<String, BankFlowCsvResultDTO> sortedInputMap = this.sortedMap(inputMap);
        // 写出Excel
        String targetExcelFileName = filePath.substring(0, filePath.lastIndexOf(".")) + "_result.xlsx";
        writeExcel(sortedOutputMap, targetExcelFileName, "出");
        writeExcel(sortedInputMap, targetExcelFileName, "进");
        log.info("已生成结果文件:{}", targetExcelFileName);
    }

    /**
     * 有就累计，没有就初始化放入map
     *
     * @param map
     * @param bankFlowCsvDTO
     */
    private void handle(Map<String, BankFlowCsvResultDTO> map, BankFlowCsvDTO bankFlowCsvDTO) {
        String targetAccountName = StringUtils.trim(bankFlowCsvDTO.getTargetAccountName());
        String txAmt = StringUtils.trim(bankFlowCsvDTO.getTxAmt());
        BankFlowCsvResultDTO bankFlowCsvResultDTO = map.get(targetAccountName);
        if (bankFlowCsvResultDTO != null) {
            bankFlowCsvResultDTO.setTxCount(bankFlowCsvResultDTO.getTxCount() + 1);
            bankFlowCsvResultDTO.setTotalAmt(bankFlowCsvResultDTO.getTotalAmt().add(new BigDecimal(txAmt)));
        } else {
            bankFlowCsvResultDTO = new BankFlowCsvResultDTO();
            bankFlowCsvResultDTO.setTargetAccountName(targetAccountName);
            bankFlowCsvResultDTO.setTxCount(1);
            bankFlowCsvResultDTO.setTotalAmt(new BigDecimal(txAmt));
            map.put(targetAccountName, bankFlowCsvResultDTO);
        }
    }

    /**
     * 根据总笔数、总金额降序排序
     *
     * @param map
     * @return
     */
    private Map<String, BankFlowCsvResultDTO> sortedMap(Map<String, BankFlowCsvResultDTO> map) {
        List<BankFlowCsvResultDTO> list = new ArrayList<>(map.values());
        Collections.sort(list, (v1, v2) -> {
            int v1TxCount = v1.getTxCount();
            int v2TxCount = v2.getTxCount();
            if (v1TxCount == v2TxCount) {
                return v2.getTotalAmt().compareTo(v1.getTotalAmt());
            }
            return v2TxCount - v1TxCount;
        });
        Map<String, BankFlowCsvResultDTO> linkedMap = new LinkedHashMap<>();
        for (BankFlowCsvResultDTO bankFlowCsvResultDTO : list) {
            linkedMap.put(bankFlowCsvResultDTO.getTargetAccountName(), bankFlowCsvResultDTO);
        }
        return linkedMap;
    }

    /**
     * 写出结果Excel文件
     *
     * @param map       排序后的map
     * @param fileName  指定输出的文件名
     * @param sheetName sheet名称
     */
    private void writeExcel(Map<String, BankFlowCsvResultDTO> map, String fileName, String sheetName) {
        ExcelWriter excelWriter = null;
        try {
            excelWriter = ExcelUtil.getWriter(fileName, sheetName);
            // 写标题
            Font headFont = excelWriter.createFont();
            headFont.setBold(true);
            excelWriter.writeHeadRow(Arrays.asList("对手户名", "交易总笔数", "交易总金额")).getHeadCellStyle().setFont(headFont);
            // 写明细
            Set<Map.Entry<String, BankFlowCsvResultDTO>> entrySet = map.entrySet();
            for (Map.Entry<String, BankFlowCsvResultDTO> entry : entrySet) {
                String key = entry.getKey();
                BankFlowCsvResultDTO mainInfo = entry.getValue();
                excelWriter.writeRow(Arrays.asList(key, mainInfo.getTxCount() + "", mainInfo.getTotalAmt().toString()));
            }
            excelWriter.autoSizeColumnAll();
        } finally {
            IoUtil.close(excelWriter);
        }
    }
}