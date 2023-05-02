package com.vendingmachine.frontend.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.vo.MemberOrderVo;

public interface MemberOrderService {
	
	Page<MemberOrder> queryMemberOrder(MemberOrderVo memberOrderVo);
	
	MemberOrder getMemberOrder(Long id);
	
	List<MemberOrder> addMemberOrder(List<MemberOrderVo> memberOrderVo);
	
	List<MemberOrder> queryMemberOrderByMemberId(String memberId);
	
	List<MemberOrder> queryMemberOrderByWalletId(Long walletId);
}
