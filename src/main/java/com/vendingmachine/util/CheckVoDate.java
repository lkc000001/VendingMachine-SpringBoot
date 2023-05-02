package com.vendingmachine.util;

import com.vendingmachine.exception.TimeFormatException;

import lombok.Data;

@Data
public  class CheckVoDate {
	
	private String createTimeStart;
	
	private String createTimeEnd;

	public CheckVoDate(String createTimeStart, String createTimeEnd) {
		this.createTimeStart = createTimeStart;
		this.createTimeEnd = createTimeEnd;
	}
	
	public void checkDateStartAndEnd() {
		createTimeStart = "____/__/__".equals(createTimeStart) ? null : createTimeStart;
		createTimeEnd = "____/__/__".equals(createTimeEnd) ? null : createTimeEnd;
		
		if(createTimeStart != null && createTimeEnd != null) {
			if(Integer.parseInt(createTimeStart.replace("/", "")) > Integer.parseInt(createTimeEnd.replace("/", ""))) {
				throw new TimeFormatException("日期設置錯誤，起始日期大於結束日期!!!", 400);
			}
		}

		if (createTimeStart != null) {
			createTimeStart = createTimeStart.replace("/","-") + " 00:00:00.000";
		}
		if (createTimeEnd != null) {
			createTimeEnd = createTimeEnd.replace("/","-") + " 23:59:59.997";
		}
	}
}
