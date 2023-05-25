package com.vendingmachine.frontend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.vendingmachine.frontend.vo.RespDataVo;
import com.vendingmachine.util.BeanCopyUtil;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@Controller
@RequestMapping(value = "/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "member");
		System.out.println(saveRespMsg);
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
		long totalCount = memberPage.getTotalElements();
		int totalPage = (int) ((totalCount / memberVo.getPageSize()) + 1);
		return ResponseEntity.ok(new JSGridReturnData<MemberVo>(memberVos, totalCount, totalPage));
    }
	
	@GetMapping(path = "/getMember/{id}")
	@ResponseBody
    public ResponseEntity<MemberVo> getMember(@PathVariable("id") String id) {
		Member member = memberService.getMember(id);
    	return ResponseEntity.ok(BeanCopyUtil.copyBean(member, MemberVo.class));
    }
	
	@PostMapping(path = "/addMember", consumes = "application/json", produces = "application/json")
	@ResponseBody
    public ResponseEntity<RespDataVo> addMember(@RequestBody MemberVo memberVo) {
		boolean checkMembetId = memberService.checkMembetId(memberVo.getMemberId());
		if(checkMembetId) {
			return ResponseEntity.ok(new RespDataVo(4001, "帳號已存在", 0));
		}
		memberVo.setEnabled("1");
		saveRespMsg = memberService.save(memberVo, "新增");
		return ResponseEntity.ok(new RespDataVo(200, saveRespMsg, 0));
    }
	
	@PutMapping(path = "/updataMember" , consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> updataMember(HttpSession session, @RequestBody MemberVo memberVo) {
		memberVo.setEnabled("1");
		saveRespMsg = memberService.save(memberVo, "修改");
		session.setAttribute("frontendUser", memberVo);
		return ResponseEntity.ok(new RespDataVo(200, saveRespMsg, 0));
    }
	
	@PutMapping(path = "/backend/save")
    public String backendUpdateMember(MemberVo memberVo) {
		saveRespMsg = memberService.save(memberVo, "修改");
		return "redirect:../";
    }
}
