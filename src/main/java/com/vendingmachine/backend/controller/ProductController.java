package com.vendingmachine.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.backend.vo.FunctionVo;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.ProductVo;
import com.vendingmachine.exception.QueryNoDataException;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "product";
    }
	
	@PostMapping(path = "/queryProduct", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<ProductVo>> queryProduct(@RequestBody ProductVo productVo) {
		JSGridReturnData<ProductVo> productVos = productService.queryProduct(productVo);
		return ResponseEntity.ok(productVos);
    }
	
	@GetMapping(path = "/getProduct/{id}")
	@ResponseBody
    public ResponseEntity<ProductVo> getProduct(@PathVariable("id") Long id) {
		ProductVo productVo = productService.getProduct(id);
    	return ResponseEntity.ok(productVo);
    }
	
	@PostMapping(path = "/save")
    public String addProduct(ProductVo productVo) {
		saveRespMsg = productService.save(productVo, "新增");
		return "redirect:./";
    }
	
	@PutMapping(path = "/save")
    public String updateProduct(ProductVo productVo) {
		saveRespMsg = productService.save(productVo, "修改");
		return "redirect:./";
    }
	
	@GetMapping(path = "/isFileExists/{fileName}")
	@ResponseBody
    public ResponseEntity<Boolean> isFileExists(@PathVariable("fileName") String fileName) {
		System.out.println(fileName);
		boolean exists = productService.isFileExists(fileName);
    	return ResponseEntity.ok(exists);
    }
}
