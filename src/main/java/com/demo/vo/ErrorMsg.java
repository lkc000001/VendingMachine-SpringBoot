package com.demo.vo;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class ErrorMsg {
	
	private Integer state;
	
	private String errorCode;
	
	private String errorMessage;

	public ErrorMsg() {}	
	
	public ErrorMsg(Integer state, String errorCode, String errorMessage) {
		this.state = state;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}


	
	
}
