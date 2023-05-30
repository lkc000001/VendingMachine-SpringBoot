package com.vendingmachine.frontend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.backend.vo.ProductVo;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.vo.RespDataVo;
import com.vendingmachine.util.BeanCopyUtil;

@CrossOrigin(value = "http://localhost:3000/")
@Controller
@RequestMapping(value = "/frontend/product")
public class FrontendProductController {

	@Autowired
	private ProductService productService;

	@PostMapping(path = "/queryProduct", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> queryProduct(@RequestBody ProductVo productVo) {
		Page<Product> productPage = productService.queryProduct(productVo);
		
		if(productPage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<ProductVo> productVos = BeanCopyUtil.copyBeanList(productPage.getContent(), ProductVo.class);
		long totalCount = productPage.getTotalElements();
		int totalPage = (int) Math.ceil((double) totalCount / (double) productVo.getPageSize());
		return ResponseEntity.ok(new RespDataVo(200, productVos, totalPage));
    }
	
	@GetMapping(path = "/getProductClassify")
	@ResponseBody
    public ResponseEntity<RespDataVo> getProductClassify() {
		List<String> classify = productService.getProductClassify();
    	return ResponseEntity.ok(new RespDataVo(200, classify, 0));
    }
	
	@GetMapping("/images/{imageName}")
	@ResponseBody
    public byte[] getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get("C:/data/image/" + imageName);
        return Files.readAllBytes(imagePath);
    }
}
