package com.vendingmachine.frontend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.frontend.service.ShoppingService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.frontend.vo.RespDataVo;

@Service
public class ShoppingServiceImpl implements ShoppingService {

	@Autowired
	private ProductService productService;

	@Override
	public RespDataVo addShoppingCart(List<MemberOrderVo> memberOrderVos, MemberOrderVo memberOrderVo) {
		Product product = productService.getProduct(memberOrderVo.getProductId());
		if(product != null) {
			memberOrderVo.setProductName(product.getProductName());
		}
		List<MemberOrderVo> memberOrderVosResp = new ArrayList<MemberOrderVo>();
		if(memberOrderVos == null || memberOrderVos.size() <= 0) {
			memberOrderVosResp.add(memberOrderVo);
		} else {
			int index = 0;
			for(MemberOrderVo m : memberOrderVos) {
				if(m.getProductId() == memberOrderVo.getProductId()) {
					memberOrderVo.setBuyQuantity(m.getBuyQuantity() + memberOrderVo.getBuyQuantity());
					memberOrderVo.setTotal(m.getTotal() + memberOrderVo.getTotal());
					memberOrderVosResp.add(memberOrderVo);
				} else {
					if(index == 0) {
						memberOrderVosResp.add(memberOrderVo);
					}
					memberOrderVosResp.add(m);
				}
				index++;
			}
		}
		return new RespDataVo(200, memberOrderVosResp, 0);
	}
	
	
}
