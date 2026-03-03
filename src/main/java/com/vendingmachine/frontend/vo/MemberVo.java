package com.vendingmachine.frontend.vo;

import java.util.List;

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
