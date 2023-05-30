package com.vendingmachine.backend.vo;

public interface UserFunctionProjection {
	Long getFunctionId();
	
	String getFunctionName();
	
	String getFunctionShowName();
	
	String getFunctionUrl();
	
	String getFunctionSort();
	
	String getFunctionGroup();
	
	String getEnabled();
	
	String getType();
	
	String getPermissionEnabled();
	
	Long getUserFunctionId();
}
