package com.demo.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.demo.entity.mssql.AppUser;
import com.demo.repositories.mssql.AppUserRepository;
import com.demo.util.ValidateUtil;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	AppUserRepository userRepository;
	
	@Autowired
	ValidateUtil validateUtil;
	
    @Override
    public UserDetails loadUserByUsername(String username) {
    	System.out.println(username);
    	AppUser user = userRepository.findByAccount(username);
    	String pwd = "";
    	String authority = "";
    	List<GrantedAuthority> auths = null;
    	if(validateUtil.isEmpty(user) || validateUtil.isBlank(user.getPwd())) {
    		throw new InternalAuthenticationServiceException("登入失敗，請確認帳號及密碼是否正確");
    	} else {
    		pwd = user.getPwd();
    		authority = user.getGroupName();
    		String Roles = "ADMIN,USER";
    		auths = Arrays.stream(Roles.split(","))
    				.map(SimpleGrantedAuthority::new)
    				//.map(role -> new SimpleGrantedAuthority(role))
    				.collect(Collectors.toList());
    	}
    	
    	UserDetails userDetails = new User(username, pwd, 
				   true, true, true, true, auths);
    	
    	return userDetails;
    }
}
