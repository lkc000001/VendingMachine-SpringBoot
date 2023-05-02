package com.vendingmachine.frontend.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.vo.WalletVo;

public interface WalletService {
	
	Page<Wallet> queryWallet(WalletVo walletVo);
	
	Wallet getWallet(Long id);
	
	Wallet updateWallet(WalletVo walletVo, String func);
	
	Long getBalance(String memberId);
}
