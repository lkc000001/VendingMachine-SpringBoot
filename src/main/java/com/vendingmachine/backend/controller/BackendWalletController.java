package com.vendingmachine.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.service.WalletService;
import com.vendingmachine.frontend.vo.RespDataVo;
import com.vendingmachine.frontend.vo.WalletVo;
import com.vendingmachine.util.BeanCopyUtil;

@Controller
@RequestMapping(value = "/backend/wallet")
public class BackendWalletController {

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
		List<Wallet> wallets = walletService.queryWallet(walletVo);
		
		long totalCount = wallets.size();
		int totalPage = (int) ((totalCount / walletVo.getPageSize()) + 1);
		
		if(totalCount <= 0) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		wallets = wallets.stream()
				.skip(walletVo.getPageSize() * (walletVo.getPageIndex() - 1))
				.limit(walletVo.getPageSize())
				//.sorted(memberOrderSort(memberOrderVo.getSortField(), memberOrderVo.getSortOrder()))
				.collect(Collectors.toList());
		
		List<WalletVo> walletVos = BeanCopyUtil.copyBeanList(wallets, WalletVo.class);
		return ResponseEntity.ok(new JSGridReturnData<WalletVo>(walletVos, totalCount, totalPage));
    }
	
	@GetMapping(path = "/getWallet/{id}")
	@ResponseBody
    public ResponseEntity<WalletVo> getWallet(@PathVariable("id") Long id) {
		Wallet wallet = walletService.getWallet(id);
    	return ResponseEntity.ok(BeanCopyUtil.copyBean(wallet, WalletVo.class));
    }
	
	@PostMapping(path = "/addWallet", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> addWallet(@RequestBody WalletVo walletVo) {
		Wallet wallet = walletService.addWallet(walletVo);
		if(wallet == null) {
			return ResponseEntity.ok(new RespDataVo(4101, "儲值失敗", 0));
		}
		Long balance = walletService.getBalance(walletVo.getMemberId());
		return ResponseEntity.ok(new RespDataVo(200, balance, 0));
	}
	
}
