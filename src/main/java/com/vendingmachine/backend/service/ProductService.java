package com.vendingmachine.backend.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.ProductVo;

public interface ProductService {
	
	JSGridReturnData<ProductVo> queryProduct(ProductVo productVo);
	
	ProductVo getProduct(Long id);
	
	boolean saveUploadedFile(MultipartFile file);
	
	boolean isFileExists(String fileName);
	
	String save(ProductVo productVo, String func);
}
