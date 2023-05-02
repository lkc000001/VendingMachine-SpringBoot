package com.vendingmachine.frontend.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.repositories.ProductRepository;
import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.ProductVo;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.repositories.MemberOrderRepository;
import com.vendingmachine.frontend.repositories.MemberRepository;
import com.vendingmachine.frontend.repositories.WalletRepository;
import com.vendingmachine.frontend.service.MemberOrderService;
import com.vendingmachine.frontend.service.MemberService;
import com.vendingmachine.frontend.service.WalletService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.frontend.vo.MemberVo;
import com.vendingmachine.frontend.vo.WalletVo;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.CheckVoDate;
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
	
	@Value("${file.upload.path}")
	String filePath;
	
	@Override
	public Page<Wallet> queryWallet(WalletVo walletVo) {
		checkData(walletVo);
		Page<Wallet> wallets = walletRepository.queryWallet(walletVo.getWalletId(), 
															walletVo.getWalletNo(), 
															walletVo.getMemberId(), 
															walletVo.getCreateTimeStart(), 
															walletVo.getCreateTimeEnd(), 
															walletVo.convertPageable());
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
	
	private void checkData(WalletVo walletVo) {

		if(validateUtil.isNotBlank(walletVo.getMemberId())) {
			walletVo.setMemberId(StringUtil.addPercentage(walletVo.getMemberId(), 3));
		}
		CheckVoDate checkVoDate = new CheckVoDate(walletVo.getCreateTimeStart(), walletVo.getCreateTimeEnd());
		walletVo.setCreateTimeStart(checkVoDate.getCreateTimeStart());
		walletVo.setCreateTimeEnd(checkVoDate.getCreateTimeEnd());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Wallet updateWallet(WalletVo walletVo, String func) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		
		Wallet wallet = BeanCopyUtil.copyBean(walletVo, Wallet.class);
		
    		//Member dbMember = getMember(member.getMemberId());

		wallet.setUpdateTime(new Date());
		wallet.setUpdateUser(username);
    	
		Wallet walletSaveResp = walletRepository.save(wallet);
		if(walletSaveResp == null) {
			return null;
		}
    	return walletSaveResp;
	}

	@Override
	public Long getBalance(String memberId) {
		return walletRepository.getBalance(memberId);
	}
}
