package com.vendingmachine.backend.service;

import com.vendingmachine.backend.vo.AppUserVo;
import com.vendingmachine.backend.vo.JSGridReturnData;

public interface AppUserService {
	
	JSGridReturnData<AppUserVo> queryAppUser(AppUserVo appUserVo);
	
	AppUserVo getAppUser(Long id);
	
	String save(AppUserVo appUserVo, String func);
	
	String delete(Long id);
}
