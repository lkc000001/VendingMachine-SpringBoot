package com.vendingmachine.exception;

public class QueryNoDataException extends RuntimeException {
	
	private Integer code;
	
	public QueryNoDataException(String message, Integer code) {
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
