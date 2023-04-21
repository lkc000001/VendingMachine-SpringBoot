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
@Table(name = "[PRODUCT]")	
public class Product implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long productId;
	
	@Column(name = "name")
	private String productName;
	
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "cost")
	private Double cost;
	
	@Column(name = "stock")
	private Integer stock;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "image")
	private String image;
	
	@Column(name = "classify")
	private String classify;
	
	@Column(name = "enabled")
	private String enabled;
	
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
}
