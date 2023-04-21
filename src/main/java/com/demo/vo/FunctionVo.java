package com.demo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"functionId"}, callSuper = false)
public class FunctionVo extends JSGridFilter {
	
	private Long functionId;
	
	private String functionName;
	
	private String functionShowName;
	
	private String functionUrl;
	
	private Integer functionSort;
	
	private String enabled;
	
	private String type;
	
	private String functionGroup;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	private String createUser;
}
