package com.vendingmachine.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.vendingmachine.backend.entity.Function;
import com.vendingmachine.backend.vo.FunctionVo;
import com.vendingmachine.backend.vo.SelectDataVo;
import com.vendingmachine.backend.vo.UserFunctionProjection;

public interface FunctionService {
	
	Page<Function> queryFunction(FunctionVo functionVo);
	
	Function getFunction(Long id);
	
	List<Function> findByEnabled();
	
	List<SelectDataVo> findByFunctionType();
	
	String save(FunctionVo functionVo, String func);
	
	Map<String,List<FunctionVo>> navBarFunctionList(Long userId);
	
	List<Function> findAll();
	
	String getFirstFunctionUrl(Long userId);
}
