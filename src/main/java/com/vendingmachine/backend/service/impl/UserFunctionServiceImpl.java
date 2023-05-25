package com.vendingmachine.backend.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.repositories.UserFunctionRepository;
import com.vendingmachine.backend.service.UserFunctionService;
import com.vendingmachine.backend.vo.PermissionVo;
import com.vendingmachine.backend.vo.UserFunctionProjection;

@Service()
public class UserFunctionServiceImpl implements UserFunctionService {
	
	@Autowired
	UserFunctionRepository userFunctionRepository;

	@Override
	public List<UserFunctionProjection> queryUserPermission(Long userId) {
		return userFunctionRepository.queryUserPermission(userId);
	}

	@Override
	public int update(UserFunction userFunction) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
