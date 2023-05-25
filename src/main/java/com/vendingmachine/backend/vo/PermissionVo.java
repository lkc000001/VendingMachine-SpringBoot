package com.vendingmachine.backend.vo;

import lombok.Data;

@Data
public class PermissionVo {
	
	private Long functionid;
	
	private String functionname;
	
	private String functionshowname;
	
	private String functionurl;
	
	private Integer functionsort;
	
	private String enabled;
	
	private String type;

	private String permissionenabled;

}
