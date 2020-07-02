package com.fun.base.exception;

public class FailException extends RuntimeException {

    private static final long serialVersionUID = -7041169491254546905L;

    public FailException() {
        super("FunTester");
    }

    protected FailException(String message) {
        super(message);
    }

    public static void fail(String message) {
        throw new FailException(message);
    }

    public static void fail() {
        throw new FailException();
    }

}
