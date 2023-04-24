package com.vendingmachine.exception;

public class LoginErrorException extends RuntimeException {
	
	private Integer code;
	
	public LoginErrorException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}
