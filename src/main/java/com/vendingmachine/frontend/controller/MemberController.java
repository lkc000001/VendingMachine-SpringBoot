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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.service.MemberService;
import com.vendingmachine.frontend.vo.MemberVo;
import com.vendingmachine.util.BeanCopyUtil;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "member");
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "member";
    }
	
	@PostMapping(path = "/queryMember", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<MemberVo>> queryMember(@RequestBody MemberVo memberVo) {
		Page<Member> memberPage = memberService.queryMember(memberVo);
		
		if(memberPage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<MemberVo> memberVos = BeanCopyUtil.copyBeanList(memberPage.getContent(), MemberVo.class);
		return ResponseEntity.ok(new JSGridReturnData<MemberVo>(memberVos, memberPage.getTotalElements()));
    }
	
	@GetMapping(path = "/getMember/{id}")
	@ResponseBody
    public ResponseEntity<MemberVo> getMember(@PathVariable("id") String id) {
		Member member = memberService.getMember(id);
    	return ResponseEntity.ok(BeanCopyUtil.copyBean(member, MemberVo.class));
    }
	
	/*@PostMapping(path = "/save")
    public String addProduct(ProductVo productVo) {
		saveRespMsg = productService.save(productVo, "新增");
		return "redirect:./";
    }*/
	
	@PutMapping(path = "/save")
    public String updateMember(MemberVo memberVo) {
		saveRespMsg = memberService.save(memberVo, "修改");
		return "redirect:./";
    }
}
