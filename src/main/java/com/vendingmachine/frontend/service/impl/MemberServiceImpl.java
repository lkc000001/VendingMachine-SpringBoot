package com.vendingmachine.frontend.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.repositories.ProductRepository;
import com.vendingmachine.backend.service.ProductService;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.ProductVo;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.entity.MemberOrder;
import com.vendingmachine.frontend.repositories.MemberOrderRepository;
import com.vendingmachine.frontend.repositories.MemberRepository;
import com.vendingmachine.frontend.service.MemberOrderService;
import com.vendingmachine.frontend.service.MemberService;
import com.vendingmachine.frontend.vo.MemberOrderVo;
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
		if(func.equals("新增")) {
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
			return func +"功能資料失敗";
		}
    	return func +"功能資料成功";
	}
	
	private void checkData(MemberVo memberVo) {

		if(validateUtil.isNotBlank(memberVo.getMemberId())) {
			memberVo.setMemberId(StringUtil.addPercentage(memberVo.getMemberId(), 3));
		}
		if(validateUtil.isNotBlank(memberVo.getMemberName())) {
			memberVo.setMemberName(StringUtil.addPercentage(memberVo.getMemberName(), 3));
		}
	}
}
