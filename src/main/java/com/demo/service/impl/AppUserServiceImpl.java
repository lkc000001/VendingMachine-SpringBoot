package com.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.demo.entity.mssql.AppUser;
import com.demo.exception.QueryNoDataException;
import com.demo.repositories.mssql.AppUserRepository;
import com.demo.service.AppUserService;
import com.demo.util.BeanCopyUtil;
import com.demo.util.StringUtil;
import com.demo.vo.AppUserVo;
import com.demo.vo.JSGridReturnData;

@Service
public class AppUserServiceImpl implements AppUserService {
	
	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private StringUtil StringUtil;
	
	@Override
	public JSGridReturnData<AppUserVo> queryAppUser(AppUserVo appUserVo) {
		checkData(appUserVo);
		Page<AppUser> appUsers = appUserRepository.queryAppUser(appUserVo.getUserId(),
																   appUserVo.getBranch(),
																   appUserVo.getGroupName(),
																   appUserVo.getAccountId(),
																   appUserVo.getAccount(),
																   appUserVo.getName(),
																   appUserVo.getEnabled(),
																   appUserVo.convertPageable());
		if(appUsers.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		List<AppUserVo> appUserVos = BeanCopyUtil.copyBeanList(appUsers.getContent(), AppUserVo.class);
		return new JSGridReturnData<AppUserVo>(appUserVos, appUsers.getTotalElements());
	}

	@Override
	public AppUserVo getAppUser(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save(AppUserVo appUserVo, String func) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkData(AppUserVo appUserVo) {
		String branch = appUserVo.getBranch();
		if(branch != null) {
			appUserVo.setBranch(StringUtil.addPercentage(branch, 3));
		}
		String groupName = appUserVo.getGroupName();
		if(groupName != null) {
			appUserVo.setGroupName(StringUtil.addPercentage(groupName, 3));
		}
		String account = appUserVo.getAccount();
		if(account != null) {
			appUserVo.setAccount(StringUtil.addPercentage(account, 3));
		}
		String name = appUserVo.getName();
		if(name != null) {
			appUserVo.setName(StringUtil.addPercentage(name, 3));
		}
	}
	
	
	
}
