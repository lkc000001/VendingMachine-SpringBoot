package com.vendingmachine.frontend.entity;

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
@Table(name = "MEMBERORDER")	
public class MemberOrder  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORDERID")
	private String orderId;
	
	@Column(name = "MEMBERID")
	private Long memberId;
	
	@Column(name = "PRODUCTID")
	private Long productId;
	
	@Column(name = "PRODUCTPRICE")
	private Integer productPrice;
	
	@Column(name = "PRODUCTCOST")
	private Double productCost;
	
	@Column(name = "BUYQUANTITY")
	private Integer buyQuantity;
	
	@Column(name = "CREATETIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
}
