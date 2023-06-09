package com.vendingmachine.backend.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.entity.Purchase;
import com.vendingmachine.backend.repositories.ProductRepository;
import com.vendingmachine.backend.repositories.PurchaseRepository;
import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.backend.service.PurchaseService;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.PurchaseVo;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.exception.TimeFormatException;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.ValidateUtil;

@Service
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	private final String NONE = "";
	
	@Override
	public Page<Purchase> queryPurchase(PurchaseVo purchaseVo) {
		checkData(purchaseVo);
		Page<Purchase> purchases = purchaseRepository.queryPurchase(purchaseVo.getId(),
																    purchaseVo.getProductId(),
																    purchaseVo.getCostStart(),
																    purchaseVo.getCostEnd(),
																    purchaseVo.getQtyStart(),
																    purchaseVo.getQtyEnd(),
																    purchaseVo.getCreateTimeStart(),
																    purchaseVo.getCreateTimeEnd(),
																    purchaseVo.convertPageable());
		return purchases;
	}

	@Override
	public Purchase getPurchase(Long id) {
		Optional<Purchase> purchase = purchaseRepository.findById(id);
		if(purchase.isPresent()) {
			return purchase.get();
		}
		return null;
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(PurchaseVo purchaseVo, String func) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Purchase purchase = BeanCopyUtil.copyBean(purchaseVo, Purchase.class);

		if(func.equals("新增")) {
			purchase.setCreateTime(new Date());
			purchase.setCreateUser(username);
    	} else {
    		purchase.setUpdateTime(new Date());
    		purchase.setUpdateUser(username);
    	}
		Purchase purchaseSaveResp = purchaseRepository.save(purchase);
		if(purchaseSaveResp == null) {
			return func +"採購單失敗";
		}
		
		Long productId = purchaseVo.getProductId();
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()) {
			Double averageCost = purchaseRepository.queryAverageCost(productId);
			int BeforeQuantity = purchaseVo.getBeforeQuantity() == null ? 0 : purchaseVo.getBeforeQuantity();
			
			Product product = productOptional.get();
			int stock = product.getStock();
			int productQuantity = purchaseVo.getQuantity() - BeforeQuantity;
			int newStock = stock + productQuantity;
			
			product.setCost(averageCost);
			product.setStock(newStock);
		} else {
			return func +"更新庫存失敗";
		}
    	return func +"採購單成功";
	}
	
	private void checkData(PurchaseVo purchaseVo) {
		String startDate = purchaseVo.getCreateTimeStart();
		String endDate = purchaseVo.getCreateTimeEnd();
        
		if(validateUtil.isNotBlank(startDate) && validateUtil.isNotBlank(endDate)) {
			if(Integer.parseInt(startDate.replace("/", "")) > Integer.parseInt(endDate.replace("/", ""))) {
				throw new TimeFormatException("日期設置錯誤，起始日期大於結束日期!!!", 400);
			}
		}

		if (validateUtil.isNotBlank(startDate)) {
			purchaseVo.setCreateTimeStart(startDate.replace("/","-") + " 00:00:00.000");
		}
		if (validateUtil.isNotBlank(endDate)) {
			purchaseVo.setCreateTimeEnd(endDate.replace("/","-") + " 23:59:59.997");
		}
	}
}
