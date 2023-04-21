package com.demo.service;

import com.demo.entity.mssql.Purchase;
import com.demo.vo.JSGridReturnData;
import com.demo.vo.ProductVo;
import com.demo.vo.PurchaseVo;

public interface PurchaseService {
	
	JSGridReturnData<PurchaseVo> queryPurchase(PurchaseVo purchaseVo);
	
	PurchaseVo getPurchase(Long id);
	
	String save(PurchaseVo purchaseVo, String func);
}
