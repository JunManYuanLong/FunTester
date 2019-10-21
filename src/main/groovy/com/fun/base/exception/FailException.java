package com.fun.base.exception;

public class FailException extends RuntimeException {

    private static final long serialVersionUID = -7041169491254546905L;

    public FailException() {
        super("FunTester");
    }

    public FailException(String message) {
        super(message);
    }


}
