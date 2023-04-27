package com.vendingmachine.backend.service;

import org.springframework.data.domain.Page;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.vo.AppUserVo;

public interface AppUserService {
	
	Page<AppUser> queryAppUser(AppUserVo appUserVo);
	
	AppUser getAppUser(Long id);
	
	String save(AppUserVo appUserVo, String func);
	
	String delete(Long id);
}
