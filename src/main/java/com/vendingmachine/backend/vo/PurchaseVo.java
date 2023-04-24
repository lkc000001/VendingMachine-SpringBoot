package com.vendingmachine.backend.vo;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class PurchaseVo extends JSGridFilter{

	private Long id;
	
	private Long productId;
	
	private Double productCost;
	
	private Double costStart;
	
	private Double costEnd;
	
	private Integer quantity;
	
	private Integer beforeQuantity;
	
	private Integer qtyStart;
	
	private Integer qtyEnd;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	private String showDate;
	
	private String createTimeStart;
	
	private String createTimeEnd;
	
	private String createUser;
	
}
