package com.vendingmachine.backend.service;

import com.vendingmachine.backend.entity.Purchase;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.ProductVo;
import com.vendingmachine.backend.vo.PurchaseVo;

public interface PurchaseService {
	
	JSGridReturnData<PurchaseVo> queryPurchase(PurchaseVo purchaseVo);
	
	PurchaseVo getPurchase(Long id);
	
	String save(PurchaseVo purchaseVo, String func);
}
