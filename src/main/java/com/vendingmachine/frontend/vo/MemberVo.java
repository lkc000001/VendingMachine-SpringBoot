package com.vendingmachine.frontend.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vendingmachine.backend.vo.JSGridFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"memberId"}, callSuper = false)
public class MemberVo extends JSGridFilter{

	private String memberId;
	
	private String memberPassword;
	
	private String memberName;
	
	private String memberAddress;
	
	private String memberBirthday;
	
	private String memberPhone;
	
	private String enabled;
	
	private List<MemberOrderVo> shoppingCart;
}
