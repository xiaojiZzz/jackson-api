package com.jackson.api.common;

/**
 * 结果工具类
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param statusCode
     * @return
     */
    public static BaseResponse error(StatusCode statusCode) {
        return new BaseResponse<>(statusCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }

    /**
     * 失败
     *
     * @param statusCode
     * @return
     */
    public static BaseResponse error(StatusCode statusCode, String message) {
        return new BaseResponse(statusCode.getCode(), null, message);
    }
}
