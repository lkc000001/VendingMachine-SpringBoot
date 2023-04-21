package com.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.mssql.Function;
import com.demo.vo.FunctionVo;
import com.demo.vo.JSGridReturnData;
import com.demo.vo.SelectDataVo;

public interface FunctionService {
	
	JSGridReturnData<Function> findByAll(Pageable pageable);
	
	JSGridReturnData<FunctionVo> queryFunction(FunctionVo functionVo);
	
	FunctionVo getFunction(Long id);
	
	List<Function> findByEnabled();
	
	List<SelectDataVo> findByFunctionType();
	
	String save(FunctionVo functionVo, String func);
	
	Map<String,List<Function>> navBarFunctionList();
}
