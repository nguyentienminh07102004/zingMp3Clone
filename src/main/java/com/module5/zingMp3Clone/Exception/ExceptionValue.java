package com.module5.zingMp3Clone.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionValue {
    PASSWORD_INVALID("Password is invalid"),
    FAILED_GENERATOR_TOKEN("Tạo token không thành công"),
    USER_NOT_FOUND("Tài khoản không đúng");
    private final String value;
}
