package com.vendingmachine.frontend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RespDataVo {
	
	private Integer state;
	
	private Object data;
	
	private Integer maxPage;
}
