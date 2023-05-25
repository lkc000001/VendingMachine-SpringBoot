package com.vendingmachine.frontend.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vendingmachine.backend.vo.JSGridFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"productId"}, callSuper = false)
public class MemberOrderVo extends JSGridFilter {

	private Long orderId;
	
	private String orderNo;
	
	private String memberId;
	
	private String memberName;
	
	private Long productId;
	
	private String productName;
	
	private Integer productPrice;
	
	private Double productCost;
	
	private Integer buyQuantity;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String createTimeStart;
	
	private String createTimeEnd;
	
	private Long walletId;
	
	private Integer total;
}
