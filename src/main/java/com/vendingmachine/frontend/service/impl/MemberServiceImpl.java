package com.vendingmachine.frontend.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.enums.SaveFunc;
import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.repositories.MemberRepository;
import com.vendingmachine.frontend.service.MemberService;
import com.vendingmachine.frontend.vo.MemberVo;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.StringUtil;
import com.vendingmachine.util.ValidateUtil;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberRepository memberRepository;

	
	@Autowired
	private StringUtil StringUtil;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Value("${file.upload.path}")
	String filePath;
	
	@Override
	public Page<Member> queryMember(MemberVo memberVo) {
		checkData(memberVo);
		Page<Member> members = memberRepository.queryMember(memberVo.getMemberId(), 
															memberVo.getMemberName(), 
															memberVo.getEnabled(), 
															memberVo.convertPageable());
		return members;
	}

	@Override
	public Member getMember(String id) {
		Optional<Member> memberOrder = memberRepository.findById(id);
		if(memberOrder.isPresent()) {
			return memberOrder.get();
		}
		return null;
	}	


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(MemberVo memberVo, String func) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		String enableStr = memberVo.getEnabled() == null ? "0" : "1";
		memberVo.setEnabled(enableStr);
		
		Member member = BeanCopyUtil.copyBean(memberVo, Member.class);
		if(SaveFunc.ADD.getFunc().equals(func)) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(memberVo.getMemberPassword());
			member.setMemberPassword(encodedPassword);
			member.setCreateTime(new Date());
			member.setCreateUser(username);
    	} else {
    		Member dbMember = getMember(member.getMemberId());
    		member.setMemberPassword(dbMember.getMemberPassword());
    		member.setCreateTime(dbMember.getCreateTime());
			member.setCreateUser(dbMember.getCreateUser());
    		member.setUpdateTime(new Date());
    		member.setUpdateUser(username);
    	}
		Member memberSaveResp = memberRepository.save(member);
		if(memberSaveResp == null) {
			return func +"會員資料失敗";
		}
    	return func +"會員資料成功";
	}
	
	private void checkData(MemberVo memberVo) {

		if(validateUtil.isNotBlank(memberVo.getMemberId())) {
			memberVo.setMemberId(StringUtil.addPercentage(memberVo.getMemberId(), 3));
		}
		if(validateUtil.isNotBlank(memberVo.getMemberName())) {
			memberVo.setMemberName(StringUtil.addPercentage(memberVo.getMemberName(), 3));
		}
	}

	@Override
	public boolean checkMembetId(String id) {
		Optional<Member> member = memberRepository.findById(id);
		boolean resp = member.isPresent() ? true : false;
		return resp;
	}
}
