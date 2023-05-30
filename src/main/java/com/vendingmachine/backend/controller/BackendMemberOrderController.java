package com.vendingmachine.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

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

@Controller
@RequestMapping(value = "/backend/memberorder")
public class BackendMemberOrderController {

	@Autowired
	private MemberOrderService memberOrderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private MemberService memberService;
	
	private String saveRespMsg;
	
	private StringBuilder showMemberOrderStr;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "memberorder");
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "memberorder";
    }
	
	@PostMapping(path = "/queryMemberOrder", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<MemberOrderVo>> queryMemberOrder(@RequestBody MemberOrderVo memberOrderVo) {
		Page<MemberOrder> memberOrderPage = memberOrderService.queryMemberOrder(memberOrderVo);
		
		if(memberOrderPage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<MemberOrderVo> memberOrderVos = BeanCopyUtil.copyBeanList(memberOrderPage.getContent(), MemberOrderVo.class);
		long totalCount = memberOrderPage.getTotalElements();
		int totalPage = (int) ((totalCount / memberOrderVo.getPageSize()) + 1);
		return ResponseEntity.ok(new JSGridReturnData<MemberOrderVo>(memberOrderVos, totalCount, totalPage));
    }
	
	@GetMapping(path = "/getMemberOrder/{id}")
	@ResponseBody
    public ResponseEntity<MemberOrderVo> getMemberOrder(@PathVariable("id") Long id) {
		MemberOrder memberOrder = memberOrderService.getMemberOrder(id);
		MemberOrderVo memberOrderVo = BeanCopyUtil.copyBean(memberOrder, MemberOrderVo.class);
		
		//取得商品名稱
		String product = getProductName(memberOrder.getProductId());
		memberOrderVo.setProductName(product);
		//取得會員名稱
		String member = getMemberName(memberOrder.getMemberId());
		memberOrderVo.setMemberName(member);

		return ResponseEntity.ok(memberOrderVo);
    }
	
	@GetMapping(path = "/queryMemberOrderByWalletId/{walletId}")
	@ResponseBody
    public ResponseEntity<RespDataVo> queryMemberOrderByWalletId(@PathVariable("walletId") Long walletId) {
		List<MemberOrder> memberOrders = memberOrderService.queryMemberOrderByWalletId(walletId);
		
		if(memberOrders.size() <= 0) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		showMemberOrderStr = new StringBuilder();
		showMemberOrderStr.append("<table class='sdtable'>");
		showMemberOrderThead();
		showMemberOrderTbody(memberOrders);
		showMemberOrderStr.append("</table>");

		RespDataVo respData = new RespDataVo(200, showMemberOrderStr.toString(),0);
		return ResponseEntity.ok(respData);
	}
	
	private String getProductName(Long productId) {
		Product product = productService.getProduct(productId);
		return product.getProductName();
	}
	
	private String getMemberName(String memberId) {
		Member member = memberService.getMember(memberId);
		return member.getMemberName();
	}
	
	private void showMemberOrderThead() {
		showMemberOrderStr.append("<thead>");
		showMemberOrderStr.append("<tr>");
		showMemberOrderStr.append("<th width=10%>訂單編號</th>");
		showMemberOrderStr.append("<th width=10%>產品編號</th>");
		showMemberOrderStr.append("<th width=20%>產品名稱</th>");
		showMemberOrderStr.append("<th width=10%>購買單價</th>");
		showMemberOrderStr.append("<th width=10%>購買數量</th>");
		showMemberOrderStr.append("<th width=10%>購買金額</th>");
		showMemberOrderStr.append("<th width=30%>購買日期</th>");
		showMemberOrderStr.append("</tr>");
		showMemberOrderStr.append("</thead>");
	}
	
	private void showMemberOrderTbody(List<MemberOrder> memberOrder) {
		showMemberOrderStr.append("<tbody>");
		memberOrder.forEach(m -> {
			showMemberOrderStr.append("<tr>");
			appendTd(m.getOrderId());
			appendTd(m.getProductId());
			appendTd(getProductName(m.getProductId()));
			appendTd(m.getProductPrice());
			appendTd(m.getBuyQuantity());
			Double totle = Double.valueOf(m.getProductPrice() * m.getBuyQuantity());
			appendTd(totle.toString());
			appendTd(m.getCreateTime());
			showMemberOrderStr.append("</tr>");
		});
		showMemberOrderStr.append("</tbody>");
	}
	
	private void appendTd(Object str) {
		showMemberOrderStr.append("<td>");
		showMemberOrderStr.append(str.toString());
		showMemberOrderStr.append("</td>");
	}
	
	@PostMapping(path = "/queryMemberOrderList", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<MemberOrderVo>> queryMemberOrderList(@RequestBody MemberOrderVo memberOrderVo) {
		List<MemberOrder> memberOrders = memberOrderService.queryMemberOrderList(memberOrderVo);
		int totalCount = memberOrders.size();
		int totalPage = (int) ((totalCount / memberOrderVo.getPageSize()) + 1);
		
		if(totalCount <= 0) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		memberOrders = memberOrders.stream()
				.skip(memberOrderVo.getPageSize() * (memberOrderVo.getPageIndex() - 1))
				.limit(memberOrderVo.getPageSize())
				//.sorted(memberOrderSort(memberOrderVo.getSortField(), memberOrderVo.getSortOrder()))
				.collect(Collectors.toList());

		List<MemberOrderVo> memberOrderVos = BeanCopyUtil.copyBeanList(memberOrders, MemberOrderVo.class);
		return ResponseEntity.ok(new JSGridReturnData<MemberOrderVo>(memberOrderVos, totalCount, totalPage));
    }
	
}
