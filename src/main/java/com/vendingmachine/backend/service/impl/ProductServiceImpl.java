package com.vendingmachine.backend.service.impl;

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
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.StringUtil;
import com.vendingmachine.util.ValidateUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StringUtil StringUtil;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Value("${file.upload.path}")
	String filePath;
	
	@Override
	public JSGridReturnData<ProductVo> queryProduct(ProductVo productVo) {
		//String productName = productVo.getProductName() == null
		checkData(productVo);
		Page<Product> products = productRepository.queryProduct(productVo.getProductId(),
																  productVo.getProductName(),
																  productVo.getClassify(),
																  productVo.getPriceStart(),
																  productVo.getPriceEnd(),
																  productVo.getCostStart(),
																  productVo.getCostEnd(),
																  productVo.getImage(),
																  productVo.getEnabled(),
																  productVo.convertPageable());
		if(products.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		List<ProductVo> productVos = BeanCopyUtil.copyBeanList(products.getContent(), ProductVo.class);
		return new JSGridReturnData<ProductVo>(productVos, products.getTotalElements());
	}

	@Override
	public ProductVo getProduct(Long id) {
		Optional<Product> product = productRepository.findById(id);
		if(product.isPresent()) {
			return BeanCopyUtil.copyBean(product.get(), ProductVo.class);
		}
		return null;
	}	
	
	@Override
	public boolean saveUploadedFile(MultipartFile file) {
        try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(filePath + file.getOriginalFilename());
			Files.write(path, bytes, new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.WRITE});
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isFileExists(String fileName) {
		Path path = Paths.get(filePath + fileName);
		boolean exists = Files.exists(path);
		return exists;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(ProductVo productVo, String func) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
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
		}
    	return func +"功能資料成功";
	}
	
	private void checkData(ProductVo productVo) {

		if(validateUtil.isNotBlank(productVo.getProductName())) {
			productVo.setProductName(StringUtil.addPercentage(productVo.getProductName(), 3));
		}
		if(validateUtil.isNotBlank(productVo.getClassify())) {
			productVo.setClassify(StringUtil.addPercentage(productVo.getClassify(), 3));
		}
		if(validateUtil.isNotBlank(productVo.getImage())) {
			productVo.setImage(StringUtil.addPercentage(productVo.getImage(), 3));
		}
	}
}
