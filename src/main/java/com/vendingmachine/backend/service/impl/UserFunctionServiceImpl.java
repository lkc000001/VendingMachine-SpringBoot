package com.vendingmachine.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.repositories.UserFunctionRepository;
import com.vendingmachine.backend.service.UserFunctionService;
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
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserFunction> save(List<UserFunction> userFunctions) {
		return userFunctionRepository.saveAll(userFunctions);
	}
	

}
