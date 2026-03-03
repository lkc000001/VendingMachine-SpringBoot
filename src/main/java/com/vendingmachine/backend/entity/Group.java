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
@Table(name = "[GROUP]")	
public class Group implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "groupid")
	private Long groupId;
	
	@Column(name = "groupcode")
	private String groupCode;
	
	@Column(name = "groupname")
	private String groupName;

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
