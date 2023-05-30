package com.vendingmachine.frontend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collections;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.service.MemberOrderService;
import com.vendingmachine.frontend.service.MemberService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.frontend.vo.RespDataVo;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.ValidateUtil;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@Controller
@RequestMapping(value = "/memberorder")
public class MemberOrderController {

	@Autowired
	private MemberOrderService memberOrderService;
	
	@Autowired
	private ProductService productService;
	
	@PostMapping(path = "/queryMemberOrderGroup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> queryMemberOrderGroup(@RequestBody MemberOrderVo memberOrderVo) {
		List<MemberOrder> memberOrders = memberOrderService.queryMemberOrderByMemberId(memberOrderVo.getMemberId());
		int memberOrdersSize = memberOrders.size();
		if(memberOrdersSize <=0 ) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<MemberOrderVo> memberOrderVos = BeanCopyUtil.copyBeanList(memberOrders, MemberOrderVo.class);

		memberOrderVos.forEach(m -> {
			//取得商品名稱
			String productName = getProductName(m.getProductId());
			m.setProductName(productName);
			m.setTotal(m.getBuyQuantity() * m.getProductPrice());
		});
		
		List<Map<String, List<MemberOrderVo>>> orderNoGroup = memberOrderVos.stream()
								.skip(memberOrderVo.getPageSize() * (memberOrderVo.getPageIndex() - 1))
								.limit(memberOrderVo.getPageSize())
			    				.collect(groupingBy(MemberOrderVo::getOrderNo, LinkedHashMap::new, Collectors.toList()))
			    				.entrySet()
			    		        .stream()
			    		        .map(entry -> Collections.singletonMap(entry.getKey(), entry.getValue()))
			    		        .collect(Collectors.toList());
		
		int totalPage = (int) ((memberOrdersSize / memberOrderVo.getPageSize()) + 1);
		RespDataVo respData = new RespDataVo(200, orderNoGroup, totalPage);
		return ResponseEntity.ok(respData);
    }
	
	@PostMapping(path = "/addMemberOrder", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addMemberOrder(HttpSession session, @RequestBody List<MemberOrderVo> memberOrderVos) {
		List<MemberOrder> memberOrders = memberOrderService.addMemberOrder(memberOrderVos);
		session.removeAttribute("shoppingCarts");
		String resp = "新增" + memberOrders.size() + "筆成功";
		return ResponseEntity.ok(resp);
    }
	
	private String getProductName(Long productId) {
		Product product = productService.getProduct(productId);
		return product.getProductName();
	}
}
