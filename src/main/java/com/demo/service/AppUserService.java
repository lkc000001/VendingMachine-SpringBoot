package com.demo.service;

import com.demo.vo.AppUserVo;
import com.demo.vo.JSGridReturnData;

public interface AppUserService {
	
	JSGridReturnData<AppUserVo> queryAppUser(AppUserVo appUserVo);
	
	AppUserVo getAppUser(Long id);
	
	String save(AppUserVo appUserVo, String func);
}
