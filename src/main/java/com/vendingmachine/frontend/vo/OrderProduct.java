package com.vendingmachine.frontend.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class OrderProduct {

	private Long productId;
	
	private String productName;
	
	private Integer productPrice;
	
	private Double productCost;
	
	private Integer buyQuantity;
	
}
