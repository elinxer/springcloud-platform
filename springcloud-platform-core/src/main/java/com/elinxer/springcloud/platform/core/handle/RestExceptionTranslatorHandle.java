package com.elinxer.springcloud.platform.core.handle;

import com.alibaba.fastjson.JSONException;
import com.elinxer.springcloud.platform.core.exception.AppException;
import com.elinxer.springcloud.platform.core.exception.FrameWorkException;
import com.elinxer.springcloud.platform.core.validation.validate.SimpleValidateResults;
import com.elinxer.springcloud.platform.core.validation.validate.ValidateResults;
import com.elinxer.springcloud.platform.core.web.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 统一异常处理
 *
 * @author elinx
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionTranslatorHandle {

    /**
     * 应用异常
     *
     * @param exception 应用异常类
     * @return 回传异常信息
     */
    @ExceptionHandler(AppException.class)
    public Response handleException(AppException exception) {
        if (exception.getCause() != null) {
            log.error("{}:{}", exception.getMessage(), exception.getCode(), exception.getCause());
        }
        return Response.error(400, exception.getMessage()).code(exception.getCode());
    }

    /**
     * 框架异常
     *
     * @param exception 框架异常
     * @return 回传异常信息
     */
    @ExceptionHandler(FrameWorkException.class)
    public Response handleException(FrameWorkException exception) {
        if (exception.getCause() != null) {
            log.error("{}:{}", exception.getMessage(), exception.getCode(), exception.getCause());
        }
        return Response.error(400, exception.getMessage()).code(exception.getCode());
    }

    /**
     * Controller层校验异常
     *
     * @param exception Controller层校验出发的异常
     * @return 回传调用方错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleException(MethodArgumentNotValidException exception) {
        SimpleValidateResults results = new SimpleValidateResults();
        exception.getBindingResult().getAllErrors()
                .stream()
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast)
                .forEach(fieldError -> results.addResult(fieldError.getField(), fieldError.getDefaultMessage()));
        return Response.error(400, results.getResults().isEmpty() ? exception.getMessage() : results.getResults().get(0).getMessage()).data(results.getResults());
    }

    /**
     * service层校验异常
     *
     * @param exception service层校验出发的异常
     * @return 回传调用方错误
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleConstraintViolationException(ConstraintViolationException exception) {
        SimpleValidateResults results = new SimpleValidateResults();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            results.addResult(violation.getPropertyPath().toString(), violation.getMessage());
        }
        List<ValidateResults.Result> errorResults = results.getResults();
        return Response
                .error(400, errorResults.isEmpty() ? "" : errorResults.get(0).getMessage())
                .data(errorResults);
    }

    /**
     * 调用校验异常
     *
     * @param exception 自定义校验异常
     * @return 回传调用方错误
     */
    @ExceptionHandler(com.elinxer.springcloud.platform.core.exception.ValidationException.class)
    public Response<List<ValidateResults.Result>> handleException(com.elinxer.springcloud.platform.core.exception.ValidationException exception) {
        return Response.<List<ValidateResults.Result>>error(400,
                        exception.getResults().isEmpty() ? exception.getMessage() : exception.getResults().get(0).getMessage())
                .data(exception.getResults());
    }

    /**
     * BindException 解析异常
     * <p>
     * 使用原因：如果未使用@RequestBody又使用实体类作为入参，异常会流入此异常
     *
     * @param exception BindException
     * @return Response
     */
    @ExceptionHandler(BindException.class)
    public Response handleException(BindException exception) {
        log.error(exception.getMessage(), exception);
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            return Response.error(400, error.getDefaultMessage());
        }
        return Response.error(400, "request params error.");
    }

    /**
     * JSONException 解析异常
     *
     * @param exception JSONException
     * @return Response
     */
    @ExceptionHandler(JSONException.class)
    public Response handleException(JSONException exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(400, "解析JSON失败");
    }

    /**
     * 非法参数异常
     *
     * @param exception IllegalArgumentException
     * @return Response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Response handleException(IllegalArgumentException exception) {
        String msg = exception.getMessage();
        if (null == msg) {
            log.error(msg = "参数错误", exception);
        }
        return Response.error(400, msg);
    }

    /**
     * 请求方式不支持异常
     *
     * @param exception HttpRequestMethodNotSupportedException
     * @return Response
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Response handleException(HttpRequestMethodNotSupportedException exception) {
        return Response
                .error(HttpStatus.METHOD_NOT_ALLOWED.value(), "不支持的请求方式")
                .data(exception.getSupportedHttpMethods());

    }

    /**
     * 404 Not Found 异常
     *
     * @param exception NoHandlerFoundException
     * @return Response
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleException(NoHandlerFoundException exception) {
        Map<String, Object> result = new HashMap<>();
        result.put("url", exception.getRequestURL());
        result.put("method", exception.getHttpMethod());
        return Response.error(HttpStatus.NOT_FOUND.value(), "请求地址不存在.").data(result);
    }

    /**
     * ContentType不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Response handleException(HttpMediaTypeNotSupportedException exception) {
        return Response.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                        "不支持的请求类型:" + exception.getContentType().toString())
                .data(exception.getSupportedMediaTypes()
                        .stream()
                        .map(MediaType::toString)
                        .collect(Collectors.toList())
                );
    }

    /**
     * 请求方法的的参数缺失
     *
     * @param exception MissingServletRequestParameterException
     * @return Response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(MissingServletRequestParameterException exception) {
        return Response.error(HttpStatus.BAD_REQUEST.value(), "参数[" + exception.getParameterName() + "]不能为空");
    }

    /**
     * 找不到具体异常的默认异常类
     *
     * @param exception RuntimeException
     * @return 回传调用方错误
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleException(RuntimeException exception) {
        String msg = Optional.ofNullable(exception.getMessage()).orElse("服务器内部错误");
        log.error(exception.getMessage(), exception);
        return Response.error(500, msg);
    }

    /**
     * 捕获 Exception异常
     *
     * @param exception Exception
     * @return Response
     */
    @ExceptionHandler(Exception.class)
    public Response handleStoreAuthException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(500, "ExceptionHandler: " + exception.getMessage());
    }

}
