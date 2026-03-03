package com.vendingmachine.frontend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.service.WalletService;
import com.vendingmachine.frontend.vo.RespDataVo;
import com.vendingmachine.frontend.vo.WalletVo;
import com.vendingmachine.util.BeanCopyUtil;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@Controller
@RequestMapping(value = "/wallet")
public class WalletController {

	@Autowired
	private WalletService walletService;
	
	@PostMapping(path = "/addWallet", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> addWallet(@RequestBody WalletVo walletVo) {
		Wallet wallet = walletService.addWallet(walletVo);
		if(wallet == null) {
			return ResponseEntity.ok(new RespDataVo(4101, "儲值失敗", 0));
		}
		Long balance = walletService.getBalance(walletVo.getMemberId());
		return ResponseEntity.ok(new RespDataVo(200, balance, 0));
	}
	
	@GetMapping(path = "/getBalance/{memberId}")
	@ResponseBody
    public ResponseEntity<RespDataVo> getBalance(@PathVariable("memberId") String memberId) {
		Long balance = walletService.getBalance(memberId);
		balance = balance == null ? 0 : balance;
    	return ResponseEntity.ok(new RespDataVo(200, balance, 0));
    }
	
	@PostMapping(path = "/findByMemberId", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> findByMemberId(@RequestBody WalletVo walletVo) {
		String memberId = walletVo.getMemberId();
		Page<Wallet> walletVoPage = walletService.findByMemberIdAndAmountGreaterThan(memberId, 0, walletVo.convertPageable());
    	
		if(walletVoPage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<WalletVo> walletVos = BeanCopyUtil.copyBeanList(walletVoPage.getContent(), WalletVo.class);
		
		Long balance = walletService.getBalance(memberId);
		walletVos.get(0).setBalance(balance);

		long totalCount = walletVoPage.getTotalElements();
		int totalPage = (int) ((totalCount / walletVo.getPageSize()) + 1);
		
		return ResponseEntity.ok(new RespDataVo(200, walletVos, totalPage));
    }
	
}
