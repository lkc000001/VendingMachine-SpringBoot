package com.vendingmachine.frontend.service;

import org.springframework.data.domain.Page;

import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.vo.MemberVo;

public interface MemberService {
	
	Page<Member> queryMember(MemberVo memberVo);
	
	Member getMember(String id);
	
	String save(MemberVo memberVo, String func);
}
