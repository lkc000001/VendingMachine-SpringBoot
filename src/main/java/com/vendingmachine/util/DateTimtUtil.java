package com.vendingmachine.util;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DateTimtUtil {

	@Autowired
	private ValidateUtil validateUtil;
	
	public Timestamp formatStrToTimestamp(String Data, int index) {
		if(index == 1) {
			Data = validateUtil.isBlank(Data) ? "1900-01-01 00:00:00" 
											  : Data.replace("/","-") + " 00:00:00.000";
		} else {
			Data = validateUtil.isBlank(Data) ? "9999-12-31 23:59:59" 
											  : Data.replace("/","-") + " 23:59:59.997";
		}
		
		Timestamp resp = Timestamp.valueOf(Data);
		return resp;
	}
}
