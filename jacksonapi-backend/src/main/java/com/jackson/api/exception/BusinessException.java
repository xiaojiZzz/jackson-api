package com.jackson.api.exception;

import com.jackson.api.common.StatusCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
    }

    public BusinessException(StatusCode statusCode, String message) {
        super(message);
        this.code = statusCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
