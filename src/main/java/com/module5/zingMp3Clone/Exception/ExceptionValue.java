package com.module5.zingMp3Clone.Exception;

import lombok.Getter;

@Getter
public enum ExceptionValue {
    PASSWORD_INVALID("Password is invalid");
    private final String value;
    ExceptionValue(String value) {
        this.value = value;
    }
}
