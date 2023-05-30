package com.vendingmachine.frontend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.vo.WalletVo;

public interface WalletService {
	
	List<Wallet> queryWallet(WalletVo walletVo);
	
	Wallet getWallet(Long id);
	
	Wallet updateWallet(WalletVo walletVo, String func);
	
	Wallet addWallet(WalletVo walletVo);
	
	Long getBalance(String memberId);
	
	Page<Wallet> findByMemberIdAndAmountGreaterThan(String memberId, Integer Amount, Pageable pageable);
}
