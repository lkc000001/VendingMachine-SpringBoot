package com.vendingmachine.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SaveFunc {

	ADD("新增"),
	UPDATE("修改");
	
	private String func;
	
	public String getFunc() {
		return func;
	}
	
}
