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
import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.repositories.MemberOrderRepository;
import com.vendingmachine.frontend.service.MemberOrderService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.StringUtil;
import com.vendingmachine.util.ValidateUtil;

@Service
public class MemberOrderServiceImpl implements MemberOrderService {

	@Autowired
	private MemberOrderRepository memberOrderRepository;

	@Autowired
	private StringUtil StringUtil;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Value("${file.upload.path}")
	String filePath;
	
	@Override
	public Page<MemberOrder> queryMemberOrder(MemberOrderVo memberOrderVo) {
		checkData(memberOrderVo);
		Page<MemberOrder> memberOrders = memberOrderRepository.queryMemberOrder(memberOrderVo.getOrderId(), 
																				memberOrderVo.getMemberId(), 
																				memberOrderVo.getProductId(), 
																				memberOrderVo.getCreateTimeStart(), 
																				memberOrderVo.getCreateTimeEnd(), 
																				memberOrderVo.convertPageable());
		
		return memberOrders;
	}

	@Override
	public MemberOrder getMemberOrder(String id) {
		Optional<MemberOrder> memberOrder = memberOrderRepository.findById(id);
		if(memberOrder.isPresent()) {
			return memberOrder.get();
		}
		return null;
	}	


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(MemberOrderVo memberOrderVo, String func) {
		/*String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if("changeImage".equals(productVo.getChangeImage())) {
			boolean uploadedFileResp =saveUploadedFile(productVo.getUploadFile());
			if(!uploadedFileResp) {
				return "上傳檔案失敗";
			}
		}
		
		String enableStr = productVo.getEnabled() == null ? "0" : "1";
		productVo.setEnabled(enableStr);
		
		Product product = BeanCopyUtil.copyBean(productVo, Product.class);
		if(func.equals("新增")) {
			product.setStock(0);
			product.setCreateTime(new Date());
			product.setCreateUser(username);
    	} else {
    		product.setUpdateTime(new Date());
    		product.setUpdateUser(username);
    	}
		Product productSaveResp = productRepository.save(product);
		if(productSaveResp == null) {
			return func +"功能資料失敗";
		}*/
    	return func +"功能資料成功";
	}
	
	private void checkData(MemberOrderVo memberOrderVo) {

		if(validateUtil.isNotBlank(memberOrderVo.getOrderId())) {
			memberOrderVo.setOrderId(StringUtil.addPercentage(memberOrderVo.getOrderId(), 3));
		}
	}
}
