package com.funtester.base.bean

import com.alibaba.fastjson.JSONObject
import com.funtester.base.interfaces.ReturnCode
import com.funtester.config.Constant

/**
 * 通用的返回体
 * 配合moco框架使用
 * @param < T >
 */
class Result<T> extends AbstractBean implements Serializable{

    private static final long serialVersionUID = -196371159847L;

    /**
     * code码
     */
    int code
    /**
     * 返回信息
     */
    T data

    Result(int code, T data) {
        this.code = code
        this.data = data
    }
/**
 * 返回简单的响应
 * @param c
 */

    Result(ReturnCode errorCode) {
        this(errorCode.getCode(), errorCode.getDesc())
    }

    def Result() {
    }
/**
 * 返回成功响应内容
 * @param data
 * @return
 */
    static <T> Result<T> success(T data) {
        new Result(0, data)
    }

    static Result success() {
        new Result()
    }

    static Result build(ReturnCode errorCode) {
        new Result(errorCode)
    }

    static Result build(int code, String msg) {
        new Result(code, msg)
    }

    static Result build(List listData) {
        success([list: listData] as JSONObject)
    }

/**
 * 返回通用失败的响应内容
 * @param data
 * @return
 */
    static <T> Result<T> fail(T data) {
        new Result<T>(Constant.TEST_ERROR_CODE, data)
    }

    static Result fail() {
        new Result(Constant.TEST_ERROR_CODE)
    }

    static Result fail(ReturnCode errorCode) {
        new Result(errorCode)
    }

/**
 * 是否成功响应
 * @return
 */
    boolean isSuccess() {
        code == 0
    }
}
