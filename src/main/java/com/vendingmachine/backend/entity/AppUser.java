package com.vendingmachine.backend.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@DynamicUpdate
@Table(name = "[USER]")	
public class AppUser implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userid")
	private Long userId;
	
	@Column(name = "branch")
	private String branch;
	
	@Column(name = "groupname")
	private String groupName;
	
	@Column(name = "accountid")
	private Integer accountId;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "pwd")
	private String pwd;

	@Column(name = "enabled")
	private String enabled;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "updatetime")
	private Date updateTime;
	
	@Column(name = "updateuser")
	private String updateUser;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "createtime")
	private Date createTime;
	
	@Column(name = "createuser")
	private String createUser;
}
