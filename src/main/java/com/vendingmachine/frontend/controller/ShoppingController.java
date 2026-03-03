package com.vendingmachine.frontend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.vendingmachine.frontend.service.ShoppingService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.frontend.vo.RespDataVo;

@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:4200"}, allowCredentials = "true")
@Controller
@RequestMapping(value = "/shopping")
public class ShoppingController {

	@Autowired
	private ShoppingService shoppingService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	HttpSession session;
	
	@SuppressWarnings("unchecked")
	@GetMapping(path = "/getShoppingCart")
	public ResponseEntity<RespDataVo> getShoppingCart(HttpSession session) {
		List<MemberOrderVo> memberOrderVos = new ArrayList<>();
		//取得購物車session
		if(session.getAttribute("shoppingCarts") != null) {
			memberOrderVos = (List<MemberOrderVo>) session.getAttribute("shoppingCarts");
		}
		return ResponseEntity.ok(new RespDataVo(200, memberOrderVos, 0));
	}
	
	@PostMapping(path = "/addShoppingCart", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> addShoppingCart(@RequestBody MemberOrderVo memberOrderVo) {
		
		//取得購物車session
		@SuppressWarnings("unchecked")
		List<MemberOrderVo> memberOrderVos = (List<MemberOrderVo>) session.getAttribute("shoppingCarts");
		//加入購物車session
		RespDataVo respDataVo = shoppingService.addShoppingCart(memberOrderVos, memberOrderVo);
		//更新購物車session內容
		session.setAttribute("shoppingCarts", respDataVo.getData());
		return ResponseEntity.ok(respDataVo);
    }
	
	@PostMapping(path = "/removeShoppingCart", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> removeShoppingCartList(@RequestBody MemberOrderVo memberOrderVo) {
		//取得購物車session
		@SuppressWarnings("unchecked")
		List<MemberOrderVo> memberOrderVos = (List<MemberOrderVo>) session.getAttribute("shoppingCarts");
		
		//移除購物車session
		List<MemberOrderVo> respmemberOrderVos = memberOrderVos.stream()
					  .filter(m -> memberOrderVo.getProductId() != m.getProductId())
					  .collect(Collectors.toList());
		
		if( respmemberOrderVos.size() == memberOrderVos.size()) {
			return ResponseEntity.ok(new RespDataVo(4201, "刪除失敗", 0));
		}
		
		if(respmemberOrderVos.size() == 0) {
			session.removeAttribute("shoppingCarts");
			return ResponseEntity.ok(new RespDataVo(4202, "無資料", 0));
		}
		
		//更新購物車session內容
		session.setAttribute("shoppingCarts", respmemberOrderVos);
		return ResponseEntity.ok(new RespDataVo(200, respmemberOrderVos, 0));
    }
	
	@GetMapping(path = "/clearShoppingCart")
	public ResponseEntity<RespDataVo> clearShoppingCart(HttpSession session) {
		session.removeAttribute("shoppingCarts");
		return ResponseEntity.ok(new RespDataVo(200, "OK", 0));
	}
	
	@GetMapping("/images/{productId}")
	@ResponseBody
    public byte[] getImage(@PathVariable Long productId) throws IOException {
		Product product = productService.getProduct(productId);
		System.out.println(product);
		Path imagePath = Paths.get("C:/data/image/" + product.getImage());
        return Files.readAllBytes(imagePath);
    }
}
