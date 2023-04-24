package com.vendingmachine.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
	
	public static final String PERCENTAGE = "%";
	
	public String addPercentage(String data, int index) {
		String resp = "";
		switch (index) {
		case 1:
			resp = PERCENTAGE + data;
			break;
		case 2:
			resp = data + PERCENTAGE;
			break;
		case 3:
			resp = PERCENTAGE + data + PERCENTAGE;
			break;
		}
		return resp;
	}
}
