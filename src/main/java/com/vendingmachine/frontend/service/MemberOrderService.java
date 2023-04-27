package com.vendingmachine.frontend.service;

import org.springframework.data.domain.Page;

import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.vo.MemberOrderVo;

public interface MemberOrderService {
	
	Page<MemberOrder> queryMemberOrder(MemberOrderVo memberOrderVo);
	
	MemberOrder getMemberOrder(String id);
	
	String save(MemberOrderVo memberOrderVo, String func);
}
