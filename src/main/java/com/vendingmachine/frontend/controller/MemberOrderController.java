package com.vendingmachine.frontend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.service.MemberOrderService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.util.BeanCopyUtil;

@Controller
@RequestMapping(value = "/memberorder")
public class MemberOrderController {

	@Autowired
	private MemberOrderService memberOrderService;
	
	private String saveRespMsg;
	
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
		return ResponseEntity.ok(new JSGridReturnData<MemberOrderVo>(memberOrderVos, memberOrderPage.getTotalElements()));
    }
	
	@GetMapping(path = "/getMemberOrder/{id}")
	@ResponseBody
    public ResponseEntity<MemberOrderVo> getMemberOrder(@PathVariable("id") String id) {
		MemberOrder memberOrder = memberOrderService.getMemberOrder(id);
		return ResponseEntity.ok(BeanCopyUtil.copyBean(memberOrder, MemberOrderVo.class));
    }
}
