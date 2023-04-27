package com.vendingmachine.backend.service;

import org.springframework.data.domain.Page;

import com.vendingmachine.backend.entity.Purchase;
import com.vendingmachine.backend.vo.PurchaseVo;

public interface PurchaseService {
	
	Page<Purchase> queryPurchase(PurchaseVo purchaseVo);
	
	Purchase getPurchase(Long id);
	
	String save(PurchaseVo purchaseVo, String func);
}
