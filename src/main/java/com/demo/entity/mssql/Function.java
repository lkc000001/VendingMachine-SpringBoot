package com.demo.entity.mssql;

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
@Table(name = "[FUNCTION]")	
public class Function  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "functionid")
	private Long functionId;
	
	@Column(name = "functionname")
	private String functionName;
	
	@Column(name = "functionshowname")
	private String functionShowName;
	
	@Column(name = "functionurl")
	private String functionUrl;
	
	@Column(name = "functionsort")
	private Integer functionSort;
	
	@Column(name = "enabled")
	private String enabled;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "updatetime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
	@Column(name = "updateuser")
	private String updateUser;
	
	@Column(name = "createtime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@Column(name = "createuser")
	private String createUser;
	
	@Column(name = "functiongroup")
	private String functionGroup;
}
