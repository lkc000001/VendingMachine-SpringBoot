package com.vendingmachine.frontend.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.exception.TimeFormatException;
import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.repositories.WalletRepository;
import com.vendingmachine.frontend.service.WalletService;
import com.vendingmachine.frontend.vo.WalletVo;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.DateTimtUtil;
import com.vendingmachine.util.StringUtil;
import com.vendingmachine.util.ValidateUtil;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private StringUtil StringUtil;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Autowired
	private DateTimtUtil dateTimtUtil;
	
	@Value("${file.upload.path}")
	String filePath;
	
	@Override
	public List<Wallet> queryWallet(WalletVo walletVo) {
		checkData(walletVo);
		List<Wallet> wallets = walletRepository.queryWallet(walletVo.getWalletNo(), 
															walletVo.getMemberId(), 
															walletVo.getStartTimestamp(), 
															walletVo.getEndTimestamp());
		return wallets;
	}

	@Override
	public Wallet getWallet(Long id) {
		Optional<Wallet> wallet = walletRepository.findById(id);
		if(wallet.isPresent()) {
			return wallet.get();
		}
		return null;
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Wallet updateWallet(WalletVo walletVo, String func) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Wallet wallet = BeanCopyUtil.copyBean(walletVo, Wallet.class);
		wallet.setUpdateTime(new Date());
		wallet.setUpdateUser(username);
    	
		Wallet walletSaveResp = walletRepository.save(wallet);
		if(walletSaveResp == null) {
			return null;
		}
    	return walletSaveResp;
	}

	@Override
	public Wallet addWallet(WalletVo walletVo) {
		String walletNoMaxId = walletRepository.getWalletNoMaxId();
		walletVo.setWalletNo(walletNoMaxId);
		walletVo.setCreateTime(new Date());
		Wallet wallet = BeanCopyUtil.copyBean(walletVo, Wallet.class);
		Wallet walletSaveResp = walletRepository.save(wallet);
		return walletSaveResp;
	}
	
	@Override
	public Long getBalance(String memberId) {
		return walletRepository.getBalance(memberId);
	}

	@Override
	public Page<Wallet> findByMemberIdAndAmountGreaterThan(String memberId, Integer Amount, Pageable pageable) {
		return walletRepository.findByMemberIdAndAmountGreaterThan(memberId, Amount, pageable);
	}	
	
	private void checkData(WalletVo walletVo) {

		if(validateUtil.isNotBlank(walletVo.getWalletNo())) {
			walletVo.setWalletNo(StringUtil.addPercentage(walletVo.getWalletNo(), 3));
		}
		if(validateUtil.isNotBlank(walletVo.getMemberId())) {
			walletVo.setMemberId(StringUtil.addPercentage(walletVo.getMemberId(), 3));
		}
		
		String createTimeStart = walletVo.getCreateTimeStart();
		String createTimeEnd = walletVo.getCreateTimeEnd();
		
		if(validateUtil.isNotBlank(createTimeStart) && validateUtil.isNotBlank(createTimeEnd)) {
			if(Integer.parseInt(createTimeStart.replace("/", "")) > Integer.parseInt(createTimeEnd.replace("/", ""))) {
				throw new TimeFormatException("日期設置錯誤，起始日期大於結束日期!!!", 400);
			}
		}
		
		walletVo.setStartTimestamp(dateTimtUtil.formatStrToTimestamp(createTimeStart, 1));
		walletVo.setEndTimestamp(dateTimtUtil.formatStrToTimestamp(createTimeEnd, 2));
	}
	
}
