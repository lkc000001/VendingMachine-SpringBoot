package com.vendingmachine.frontend.service;

import java.util.List;

import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.frontend.vo.RespDataVo;

public interface ShoppingService {
	
	RespDataVo addShoppingCart(List<MemberOrderVo> memberOrderVos, MemberOrderVo memberOrderVo);
	
}
