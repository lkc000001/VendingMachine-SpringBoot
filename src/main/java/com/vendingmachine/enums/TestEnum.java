package com.vendingmachine.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum TestEnum {
	
	E001("");
	
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	
	public static TestEnum getEnum(String name) {
		return Arrays.stream(TestEnum.values())
					 .filter(e -> e.name().equals(name))
					 .findFirst()
					 .orElse(null);
	}
}
