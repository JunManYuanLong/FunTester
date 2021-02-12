package com.funtester.base.exception;

import com.funtester.config.Constant;

/**
 * 自定义异常基类
 */
public class FailException extends RuntimeException {

    private static final long serialVersionUID = -7041169491254546905L;

    public FailException() {
        super(Constant.DEFAULT_STRING);
    }

    protected FailException(String message) {
        super(message);
    }

    public static void fail(String message) {
        throw new FailException(message);
    }

    /**
     * 默认抛异常,多用于调试
     */
    public static void fail() {
        throw new FailException();
    }

    /**
     * 将检查异常修改为运行异常
     *
     * @param e
     */
    public static void fail(Exception e) {
        throw new FailException(e.getMessage());
    }


}
