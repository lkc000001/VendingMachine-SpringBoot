package com.vendingmachine.backend.service;

import java.util.List;
import java.util.Set;

import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.vo.PermissionVo;
import com.vendingmachine.backend.vo.UserFunctionProjection;

public interface UserFunctionService {
	
	List<UserFunctionProjection> queryUserPermission(Long userId);
	
	int update(UserFunction userFunction);
}
