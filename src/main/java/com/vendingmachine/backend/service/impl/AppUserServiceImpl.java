package com.vendingmachine.backend.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.repositories.AppUserRepository;
import com.vendingmachine.backend.service.AppUserService;
import com.vendingmachine.backend.vo.AppUserVo;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.StringUtil;

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
		Optional<AppUser> appUser = appUserRepository.findById(id);
		if(appUser.isPresent()) {
			return BeanCopyUtil.copyBean(appUser.get(), AppUserVo.class);
		}
		return null;
	}

	@Override
	public String save(AppUserVo appUserVo, String func) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		String enableStr = appUserVo.getEnabled() == null ? "0" : "1";
		appUserVo.setEnabled(enableStr);
		
		AppUser appUser = BeanCopyUtil.copyBean(appUserVo, AppUser.class);

		if(func.equals("新增")) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			//String defaultPassword = "vm@" + appUser.getAccountId();
			//String encodedPassword = encoder.encode(defaultPassword);
			String encodedPassword = encoder.encode(appUser.getPwd());
			
			appUser.setPwd(encodedPassword);
			appUser.setCreateTime(new Date());
			appUser.setCreateUser(username);
    	} else {
    		Optional<AppUser> appUserOptional = appUserRepository.findById(appUser.getUserId());
    		if(appUserOptional.isPresent()) {
    			AppUser dbAppUser = appUserOptional.get();
    			
    			appUser.setPwd(dbAppUser.getPwd());
    			appUser.setCreateTime(dbAppUser.getCreateTime());
    			appUser.setCreateUser(dbAppUser.getCreateUser());
    			appUser.setUpdateTime(new Date());
    			appUser.setUpdateUser(username);
    		} else {
    			return func +"使用者失敗(查詢)";
    		}
    	}
		AppUser appUserSaveResp = appUserRepository.save(appUser);
		if(appUserSaveResp == null) {
			return func +"使用者失敗";
		}
		return func +"使用者成功";
	}
	
	@Override
	public String delete(Long id) {
		appUserRepository.deleteById(id);
		return "刪除成功";
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
