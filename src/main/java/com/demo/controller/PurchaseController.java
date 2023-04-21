package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.demo.service.PurchaseService;
import com.demo.vo.JSGridReturnData;
import com.demo.vo.PurchaseVo;

@Controller
@RequestMapping(value = "/purchase")
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "purchase";
    }
	
	@PostMapping(path = "/queryPurchase", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<PurchaseVo>> queryPurchase(@RequestBody PurchaseVo purchaseVo) {
		JSGridReturnData<PurchaseVo> purchaseVos = purchaseService.queryPurchase(purchaseVo);
		return ResponseEntity.ok(purchaseVos);
    }
	
	@GetMapping(path = "/getPurchase/{id}")
	@ResponseBody
    public ResponseEntity<PurchaseVo> getPurchase(@PathVariable("id") Long id) {
		PurchaseVo purchaseVo = purchaseService.getPurchase(id);
    	return ResponseEntity.ok(purchaseVo);
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
