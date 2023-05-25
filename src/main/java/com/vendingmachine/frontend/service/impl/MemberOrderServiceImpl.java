package com.vendingmachine.frontend.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.repositories.ProductRepository;
import com.vendingmachine.exception.InsufficientBalanceException;
import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.entity.Wallet;
import com.vendingmachine.frontend.repositories.MemberOrderRepository;
import com.vendingmachine.frontend.repositories.WalletRepository;
import com.vendingmachine.frontend.service.MemberOrderService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.frontend.vo.WalletVo;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.StringUtil;
import com.vendingmachine.util.ValidateUtil;

@Service
public class MemberOrderServiceImpl implements MemberOrderService {

	@Autowired
	private MemberOrderRepository memberOrderRepository;

	@Autowired
	private WalletRepository walletRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private StringUtil StringUtil;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Value("${file.upload.path}")
	String filePath;
	
	@Override
	public Page<MemberOrder> queryMemberOrder(MemberOrderVo memberOrderVo) {
		//checkData(memberOrderVo);
		System.out.println(memberOrderVo);
		Page<MemberOrder> memberOrders = memberOrderRepository.queryMemberOrder(memberOrderVo.getOrderNo(), 
																				memberOrderVo.getMemberId(), 
																				memberOrderVo.getProductId(), 
																				memberOrderVo.getCreateTimeStart(), 
																				memberOrderVo.getCreateTimeEnd(), 
																				memberOrderVo.convertPageable());
		
		System.out.println(memberOrders.getContent());
		return memberOrders;
	}

	@Override
	public MemberOrder getMemberOrder(Long id) {
		Optional<MemberOrder> memberOrder = memberOrderRepository.findById(id);
		if(memberOrder.isPresent()) {
			return memberOrder.get();
		}
		return null;
	}	


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MemberOrder> addMemberOrder(List<MemberOrderVo> memberOrderVo) {
		List<MemberOrder> memberOrder = BeanCopyUtil.copyBeanList(memberOrderVo, MemberOrder.class);
		//確認餘額, 儲存金額
		Wallet walletSaveResp = addWallet(memberOrderVo);
		final Long walletId = walletSaveResp.getWalletId();
		//取得當天最大訂單流水號
		String orderNoMaxId = memberOrderRepository.getOrderNoMaxId();
		//設定扣款ID,訂單編號,扣款日期
		memberOrder.forEach(m -> {
			m.setWalletId(walletId);
			m.setOrderNo(orderNoMaxId);
			m.setCreateTime(new Date());
		}) ;
		//儲存購買清單
		List<MemberOrder> memberOrderSaveResp = memberOrderRepository.saveAll(memberOrder);
		//扣除庫存
		memberOrderVo.forEach(m -> {
			Optional<Product> productOptional = productRepository.findById(m.getProductId());
			if(productOptional.isPresent()) {
				Product product = productOptional.get();
				Integer stock = product.getStock();
				product.setStock(stock - m.getBuyQuantity());
			}
		});
		
    	return memberOrderSaveResp;
	}
	
	private Wallet addWallet(List<MemberOrderVo> memberOrderVo) {
		//計算總金額
		Integer total = memberOrderVo.stream()
			    					 .mapToInt(m -> m.getTotal())
			    					 .sum();
		//查詢餘額
		Long memberBalance = walletRepository.getBalance(memberOrderVo.get(0).getMemberId());
		if(memberBalance == null || memberBalance <= 0 || (memberBalance - total) <= 0) {
			throw new InsufficientBalanceException("餘額不足!!!", 404);
		}
		//取得walletNo最大流水號
		String walletNoMaxId = walletRepository.getWalletNoMaxId();
		WalletVo walletVo = new WalletVo(walletNoMaxId, 
										 memberOrderVo.get(0).getMemberId(), 
										 -total,
										 new Date());
		Wallet wallet = BeanCopyUtil.copyBean(walletVo, Wallet.class);
		Wallet walletSaveResp = walletRepository.save(wallet);
		return walletSaveResp;
	}

	private void checkData(MemberOrderVo memberOrderVo) {
		if(validateUtil.isNotBlank(memberOrderVo.getOrderNo())) {
			memberOrderVo.setOrderNo(StringUtil.addPercentage(memberOrderVo.getOrderNo(), 3));
		} 
		else {
			memberOrderVo.setOrderNo(null);
		}
	}

	@Override
	public List<MemberOrder> queryMemberOrderByMemberId(String memberId) {
		return memberOrderRepository.findByMemberId(memberId);
	}

	@Override
	public List<MemberOrder> queryMemberOrderByWalletId(Long walletId) {
		return memberOrderRepository.findByWalletId(walletId);
	}
	
	public List<MemberOrder> queryMemberOrderList(MemberOrderVo memberOrderVo) {
		List<MemberOrder> memberOrders = memberOrderRepository.queryMemberOrderList(memberOrderVo.getOrderNo(), 
																				memberOrderVo.getMemberId(), 
																				memberOrderVo.getProductId(), 
																				memberOrderVo.getCreateTimeStart(), 
																				memberOrderVo.getCreateTimeEnd());
		
		return memberOrders;
	}
}
