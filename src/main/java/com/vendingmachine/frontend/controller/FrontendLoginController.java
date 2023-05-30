package com.vendingmachine.frontend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.service.MemberService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
import com.vendingmachine.frontend.vo.MemberVo;
import com.vendingmachine.frontend.vo.RespDataVo;
import com.vendingmachine.util.BeanCopyUtil;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RestController
@RequestMapping(value = "/frontend")
public class FrontendLoginController {

	@Autowired
	private MemberService memberService;
	
	@PostMapping(path = "/memberLogin", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> index(HttpSession session, @RequestBody MemberVo memberVo) {
		Member member = memberService.getMember(memberVo.getMemberId());

		if(member == null) {
			return ResponseEntity.ok(new RespDataVo(4001, "帳號或密碼錯誤", 0));
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean passwordsMatch = encoder.matches(memberVo.getMemberPassword(), member.getMemberPassword());

		if(!passwordsMatch) {
			return ResponseEntity.ok(new RespDataVo(4002, "帳號或密碼錯誤", 0));
		}
		if(!"1".equals(member.getEnabled())) {
			return ResponseEntity.ok(new RespDataVo(4003, "帳號未啟用", 0));
		}
		member.setMemberPassword("");
		MemberVo respmemberVo = BeanCopyUtil.copyBean(member, MemberVo.class);
		session.setAttribute("frontendUser", respmemberVo);
    	return ResponseEntity.ok(new RespDataVo(200, respmemberVo, 0));
    }
	
	@GetMapping(path = "/checkLogin")
	public ResponseEntity<RespDataVo> checkLogin (HttpSession session) {
		MemberVo frontendUser = (MemberVo) session.getAttribute("frontendUser");
		if(frontendUser == null ) {
			return ResponseEntity.ok(new RespDataVo(4004, null, 0));
		}
		@SuppressWarnings("unchecked")
		List<MemberOrderVo> memberOrderVos = (List<MemberOrderVo>) session.getAttribute("shoppingCarts");
		frontendUser.setShoppingCart(memberOrderVos);
		return ResponseEntity.ok(new RespDataVo(200, frontendUser, 0));
	}
	
	@GetMapping(path = "/logout")
	public ResponseEntity<RespDataVo> logout (HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok(new RespDataVo(200, "OK", 0));
	}
	
}
