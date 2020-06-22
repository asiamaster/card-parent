package com.dili.card.exception.global;

import com.dili.card.exception.CardAppBizException;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author: miaoguoxin
 * @date: 2020/4/8 11:22
 */
@RestControllerAdvice
public class GenericGlobalExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericGlobalExceptionResolver.class);


    /**
     * 方法参数校验处理，配合validator
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseOutput<?> handleMethodArgException(MethodArgumentNotValidException ex) {

        return BaseOutput.create(ResultCode.PARAMS_ERROR,
                errorMessage(ex.getBindingResult()));
    }

    /**
     * 参数断言异常处理
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public BaseOutput<?> handleIllegalArgsException(IllegalArgumentException ex) {
        return BaseOutput.create(ResultCode.PARAMS_ERROR, ex.getMessage());
    }

    /**
     * 单参数校验异常处理
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public BaseOutput<?> handleConstraintViolationException(ConstraintViolationException ex) {
        return BaseOutput.create(ResultCode.PARAMS_ERROR, ex.getMessage());
    }

    /**
     * 415处理
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public BaseOutput<?> handle415Exception(HttpMediaTypeNotSupportedException ex) {
        return BaseOutput.create(ResultCode.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
    }

    /**
     * 400处理
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public BaseOutput<?> handle400Exception(HttpMessageNotReadableException ex) {
        LOGGER.error("参数有误：{}", ex.getMessage());
        return BaseOutput.create(ResultCode.INVALID_REQUEST, "Bad Request");
    }

    /**
     * 405处理
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public BaseOutput<?> handle405Exception(HttpRequestMethodNotSupportedException ex) {
        return BaseOutput.create(ResultCode.METHOD_NOT_ALLOWED, "Method Not Allowed");
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler({BusinessException.class})
    public BaseOutput<?> handlerBusinessException(BusinessException e) {
        return BaseOutput.create(e.getErrorCode(), e.getMessage());
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler({CardAppBizException.class})
    public BaseOutput<?> handlerAppException(CardAppBizException e) {
        return BaseOutput.create(e.getCode(), e.getMessage());
    }

    /**
     * 处理未自定义的异常
     */
    @ExceptionHandler({Exception.class})
    public BaseOutput<?> handlerOtherException(Exception e) {
        LOGGER.error("其他异常:{}", e);
        return BaseOutput.failure("服务异常，请联系管理员");
    }

    private static String errorMessage(BindingResult result) {
        if (result == null) {
            return "";
        }
        List<ObjectError> allErrors = result.getAllErrors();
        return allErrors.stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("未知参数错误");
    }
}
