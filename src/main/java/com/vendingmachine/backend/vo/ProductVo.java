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
@EqualsAndHashCode(of = {"productId"}, callSuper = false)
public class ProductVo extends JSGridFilter{

	private Long productId;
	
	private String productName;
	
	private Integer price;
	
	private Integer priceStart;
	
	private Integer priceEnd;
	
	private Integer cost;
	
	private Integer costStart;
	
	private Integer costEnd;
	
	private Integer stock;
	
	private String unit;
	
	private String image;
	
	private String classify;
	
	private String enabled;
	
	private MultipartFile uploadFile;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	private String createUser;
	
	private String changeImage;
}
