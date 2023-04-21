package com.demo.exception;

public class AuthCodeException extends RuntimeException {
	
	private final Integer code;
	
	public AuthCodeException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
