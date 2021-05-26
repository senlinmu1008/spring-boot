package net.zhaoxiaobin.parsecsv.exception;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author zhaoxb
 * @date 2019-09-30 15:46
 * @return
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object GlobalExceptionHandler(Exception e) {
        log.error("全局异常：", e);
        String msg = "系统异常";
        if(e.getCause() instanceof CsvRequiredFieldEmptyException) {
            msg = e.getMessage() + e.getCause().getMessage();
        }
        return msg;
    }

}
