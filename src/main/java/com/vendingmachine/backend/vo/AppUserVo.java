package com.vendingmachine.backend.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"userId"}, callSuper = false)
public class AppUserVo extends JSGridFilter {
	
	private Long userId;

	private String branch;

	private String groupName;

	private Integer accountId;

	private String account;

	private String name;

	private String pwd;
	
	private String enabled;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String createUser;
}
