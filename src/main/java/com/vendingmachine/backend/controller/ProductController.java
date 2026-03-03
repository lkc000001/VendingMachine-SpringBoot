package com.vendingmachine.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.ProductVo;
import com.vendingmachine.enums.SaveFunc;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.util.BeanCopyUtil;


@CrossOrigin(value = "http://localhost:3000", exposedHeaders = "ETag")
@Controller
@RequestMapping(value = "/Product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "Product");
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "product";
    }
	
	@PostMapping(path = "/queryProduct", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<ProductVo>> queryProduct(@RequestBody ProductVo productVo, @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
		String etag = productService.getLastUpdateTime(1L).toString();
		
		System.out.println("ifNoneMatch: " + ifNoneMatch);
		System.out.println("etag: " + etag);
		
		if (etag.equals(ifNoneMatch)) {
	        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
	        					 .cacheControl(CacheControl.noCache()) 
	                			 .eTag(etag)
	                			 .build();
	    }
		
		Page<Product> productPage = productService.queryProduct(productVo);
		
		if(productPage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<ProductVo> productVos = BeanCopyUtil.copyBeanList(productPage.getContent(), ProductVo.class);
		long totalCount = productPage.getTotalElements();
		int totalPage = (int) Math.ceil((double) totalCount / (double) productVo.getPageSize());
		
		JSGridReturnData<ProductVo> vo = new JSGridReturnData<ProductVo>(productVos, totalCount, totalPage);

	    return ResponseEntity.ok()
	    					 .cacheControl(CacheControl.noCache()) 
	            			 .eTag(etag)
	            			 .body(vo);
		//return ResponseEntity.ok(new JSGridReturnData<ProductVo>(productVos, totalCount, totalPage));
    }
	
	@GetMapping(path = "/getProduct/{id}")
	@ResponseBody
    public ResponseEntity<ProductVo> getProduct(@PathVariable("id") Long id, @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
		LocalDateTime lastUpdate = productService.getLastUpdateTime(id);
		String etag = "\"" + lastUpdate.toString() + "\"";

		if (etag.equals(ifNoneMatch)) {
	        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
	        					 .cacheControl(CacheControl.noCache()) 
	                			 .eTag(etag)
	                			 .build();
	    }
		
		Product product = productService.getProduct(id);
		
		ProductVo vo = BeanCopyUtil.copyBean(product, ProductVo.class);

	    return ResponseEntity.ok()
	    					 .cacheControl(CacheControl.noCache()) 
	            			 .eTag(etag)
	            			 .body(vo);
	    
    	//return ResponseEntity.ok(BeanCopyUtil.copyBean(product, ProductVo.class));
    }
	
	@PostMapping(path = "/save")
    public String addProduct(ProductVo productVo) {
		saveRespMsg = productService.save(productVo, SaveFunc.ADD.getFunc());
		return "redirect:./";
    }
	
	@PutMapping(path = "/save")
    public String updateProduct(ProductVo productVo) {
		saveRespMsg = productService.save(productVo, SaveFunc.UPDATE.getFunc());
		return "redirect:./";
    }
	
	@GetMapping(path = "/isFileExists/{fileName}")
	@ResponseBody
    public ResponseEntity<Boolean> isFileExists(@PathVariable("fileName") String fileName) {
		boolean exists = productService.isFileExists(fileName);
    	return ResponseEntity.ok(exists);
    }
}
