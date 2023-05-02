package com.vendingmachine.frontend.controller;

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

import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.service.MemberService;
import com.vendingmachine.frontend.service.WalletService;
import com.vendingmachine.frontend.vo.MemberVo;
import com.vendingmachine.frontend.vo.WalletVo;
import com.vendingmachine.util.BeanCopyUtil;

@Controller
@RequestMapping(value = "/wallet")
public class WalletController {

	@Autowired
	private WalletService walletService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "wallet");
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "wallet";
    }
	
	@PostMapping(path = "/queryWallet", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<WalletVo>> queryWallet(@RequestBody WalletVo walletVo) {
		Page<Wallet> walletVoPage = walletService.queryWallet(walletVo);
		
		if(walletVoPage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<WalletVo> memberVos = BeanCopyUtil.copyBeanList(walletVoPage.getContent(), WalletVo.class);
		return ResponseEntity.ok(new JSGridReturnData<WalletVo>(memberVos, walletVoPage.getTotalElements()));
    }
	
	@GetMapping(path = "/getWallet/{id}")
	@ResponseBody
    public ResponseEntity<WalletVo> getWallet(@PathVariable("id") Long id) {
		Wallet wallet = walletService.getWallet(id);
    	return ResponseEntity.ok(BeanCopyUtil.copyBean(wallet, WalletVo.class));
    }
}
