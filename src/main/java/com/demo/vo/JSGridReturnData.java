package com.demo.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JSGridReturnData<T> {
	
	private List<T> data;    
	
	private long itemsCount;
}
