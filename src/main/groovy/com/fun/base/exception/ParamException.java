package com.fun.base.exception;

public class ParamException extends FailException {

	private static final long serialVersionUID = -5079364420579956243L;

	public ParamException() {
		super("参数错误!");
	}

	public ParamException(String name) {
		super(String.format("参数{%s}错误!", name));
	}


}
