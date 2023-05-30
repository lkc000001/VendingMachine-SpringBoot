package com.vendingmachine.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.repositories.AppUserRepository;
import com.vendingmachine.backend.repositories.UserFunctionRepository;
import com.vendingmachine.backend.vo.UserFunctionProjection;
import com.vendingmachine.util.ValidateUtil;

//@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	AppUserRepository userRepository;
	
	@Autowired
	UserFunctionRepository userFunctionRepository;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@Autowired
	HttpSession session;
	
    @Override
    public UserDetails loadUserByUsername(String username) {
    	AppUser user = userRepository.findByAccount(username);
    	String pwd = "";
    	String authority = "";
    	List<GrantedAuthority> auths = null;
    	if(validateUtil.isEmpty(user) || validateUtil.isBlank(user.getPwd())) {
    		throw new InternalAuthenticationServiceException("登入失敗，請確認帳號及密碼是否正確");
    	} else {
    		pwd = user.getPwd();
    		authority = user.getGroupName();
    		
    		List<UserFunctionProjection> userPermission = userFunctionRepository.queryUserPermission(user.getUserId());
    		List<String> userRoles = userPermission.stream()
    												.filter(u -> "1".equals(u.getPermissionEnabled()))
    												.map(u -> "ROLE_" + u.getFunctionName())
    												.collect(Collectors.toList()); 
    		System.out.println(userRoles);
    		auths = userRoles.stream()
    				.map(SimpleGrantedAuthority::new)
    				.collect(Collectors.toList());
    	}
    	
    	UserDetails userDetails = new User(username, pwd, 
				   true, true, true, true, auths);
    	
    	return userDetails;
    }
}
