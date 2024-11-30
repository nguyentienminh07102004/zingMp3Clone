package com.module5.zingMp3Clone.Exception;

import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyHandlerException {

    @ExceptionHandler(value = DataInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse DataInvalidExceptionHandler(DataInvalidException exception) {
        return APIResponse.builder()
                .message(exception.getMessage())
                .build();
    }
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse HandlerException(RuntimeException exception) {
        return APIResponse.builder()
                .message(exception.getMessage())
                .build();
    }
}
