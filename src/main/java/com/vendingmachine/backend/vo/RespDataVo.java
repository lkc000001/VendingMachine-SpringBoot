package com.vendingmachine.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RespDataVo {
	
	private Integer state;
	
	private Object data;
	
}
