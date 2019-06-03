package com.fun.base.bean
/**
 * 通用的返回体
 * @param <T>
 */
class Result<T> extends AbstractBean {

    int code

    T data

    Result(int code, T data) {
        this.code = code
        this.data = data
    }

    Result(int c) {
        this.code = c
    }


    static <T> Result<T> success(T data) {
        new Result<>(0, data)
    }

    Result() {}

    static <T> Result<T> fail(T data) {
        new Result<T>(TEST_ERROR_CODE, data)
    }

    boolean success() {
        code == 0
    }
}
