package com.vendingmachine.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.vo.ProductVo;

public interface ProductService {
	
	Page<Product> queryProduct(ProductVo productVo);
	
	Product getProduct(Long id);
	
	boolean saveUploadedFile(MultipartFile file);
	
	boolean isFileExists(String fileName);
	
	String save(ProductVo productVo, String func);
	
	List<String> getProductClassify();
}
