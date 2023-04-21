package com.demo.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.demo.vo.JSGridReturnData;
import com.demo.vo.ProductVo;

public interface ProductService {
	
	JSGridReturnData<ProductVo> queryProduct(ProductVo productVo);
	
	ProductVo getProduct(Long id);
	
	boolean saveUploadedFile(MultipartFile file);
	
	boolean isFileExists(String fileName);
	
	String save(ProductVo productVo, String func);
}
