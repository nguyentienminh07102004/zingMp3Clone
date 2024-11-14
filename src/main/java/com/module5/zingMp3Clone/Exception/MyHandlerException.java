package com.module5.zingMp3Clone.Exception;

import com.module5.zingMp3Clone.Model.Response.APIResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyHandlerException {

    @ExceptionHandler(value = DataInvalidException.class)
    public APIResponse DataInvalidExceptionHandler(DataInvalidException exception) {
        return APIResponse.builder()
                .message(exception.getMessage())
                .build();
    }
}
