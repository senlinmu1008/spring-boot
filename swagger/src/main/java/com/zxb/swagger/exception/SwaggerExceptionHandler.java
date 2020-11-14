package com.zxb.swagger.exception;

import com.zxb.swagger.domain.response.Resp_Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
public class SwaggerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Resp_Entity MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            sb.append(error.getField() + "->" + error.getDefaultMessage());
        }
        Resp_Entity respEntity = new Resp_Entity();
        respEntity.setReturnCode("888888");
        respEntity.setReturnMsg(sb.toString());
        return respEntity;
    }

}
