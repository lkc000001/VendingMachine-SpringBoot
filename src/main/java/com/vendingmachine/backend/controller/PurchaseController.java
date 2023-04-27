package com.vendingmachine.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.entity.Purchase;
import com.vendingmachine.backend.service.PurchaseService;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.PurchaseVo;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.util.BeanCopyUtil;

@Controller
@RequestMapping(value = "/purchase")
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "purchase");
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "purchase";
    }
	
	@PostMapping(path = "/queryPurchase", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<PurchaseVo>> queryPurchase(@RequestBody PurchaseVo purchaseVo) {
		Page<Purchase> purchasePage = purchaseService.queryPurchase(purchaseVo);
		
		if(purchasePage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<PurchaseVo> purchaseVos = BeanCopyUtil.copyBeanList(purchasePage.getContent(), PurchaseVo.class);
		return ResponseEntity.ok(new JSGridReturnData<PurchaseVo>(purchaseVos, purchasePage.getTotalElements()));
    }
	
	@GetMapping(path = "/getPurchase/{id}")
	@ResponseBody
    public ResponseEntity<PurchaseVo> getPurchase(@PathVariable("id") Long id) {
		Purchase purchase = purchaseService.getPurchase(id);
    	return ResponseEntity.ok(BeanCopyUtil.copyBean(purchase, PurchaseVo.class));
    }
	
	@PostMapping(path = "/save")
    public String addPurchase(PurchaseVo purchaseVo) {
		saveRespMsg = purchaseService.save(purchaseVo, "新增");
		return "redirect:./";
    }
	
	@PutMapping(path = "/save")
    public String updatePurchase(PurchaseVo purchaseVo) {
		saveRespMsg = purchaseService.save(purchaseVo, "修改");
		return "redirect:./";
    }
}
