package com.vendingmachine.frontend.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
	@SequenceGenerator(name = "MEMBERORDER_SEQ", sequenceName = "MEMBERORDER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBERORDER_SEQ")
	@Column(name = "ORDER_ID")
	private Long orderId;
	
	@Column(name = "ORDER_NO")
	private String orderNo;
	
	@Column(name = "MEMBER_ID")
	private String memberId;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	@Column(name = "PRODUCT_PRICE")
	private Integer productPrice;
	
	@Column(name = "PRODUCT_COST")
	private Double productCost;
	
	@Column(name = "BUY_QUANTITY")
	private Integer buyQuantity;
	
	@Column(name = "CREATE_TIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@Column(name = "WALLET_ID")
	private Long walletId;
}
