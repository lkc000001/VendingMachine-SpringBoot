package com.vendingmachine.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.backend.entity.Function;
import com.vendingmachine.backend.vo.FunctionVo;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.backend.vo.SelectDataVo;

public interface FunctionService {
	
	JSGridReturnData<Function> findByAll(Pageable pageable);
	
	JSGridReturnData<FunctionVo> queryFunction(FunctionVo functionVo);
	
	FunctionVo getFunction(Long id);
	
	List<Function> findByEnabled();
	
	List<SelectDataVo> findByFunctionType();
	
	String save(FunctionVo functionVo, String func);
	
	Map<String,List<Function>> navBarFunctionList();
}
