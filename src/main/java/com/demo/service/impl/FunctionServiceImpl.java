package com.demo.service.impl;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.mssql.AppUser;
import com.demo.entity.mssql.Function;
import com.demo.exception.QueryNoDataException;
import com.demo.repositories.mssql.FunctionRepository;
import com.demo.service.FunctionService;
import com.demo.service.AppUserService;
import com.demo.util.BeanCopyUtil;
import com.demo.util.StringUtil;
import com.demo.vo.FunctionVo;
import com.demo.vo.JSGridReturnData;
import com.demo.vo.ProductVo;
import com.demo.vo.SelectDataVo;

@Service
public class FunctionServiceImpl implements FunctionService {

	@Autowired
	private FunctionRepository functionRepository;

	@Autowired
	private StringUtil StringUtil;
	
	@Autowired
	HttpSession session;
	
	@Override
	public JSGridReturnData<Function> findByAll(Pageable pageable) {
		Page<Function> functions = functionRepository.findAll(pageable);
		long count = functionRepository.count();
		return new JSGridReturnData<Function>(functions.getContent(), count);
	}
	
	@Override
	public JSGridReturnData<FunctionVo> queryFunction(FunctionVo functionVo) {
		checkData(functionVo);
		Page<Function> functions = functionRepository.queryFunction(functionVo.getFunctionName(),
																	functionVo.getFunctionShowName(),
																	functionVo.getFunctionUrl(),
																	functionVo.getEnabled(),
																	functionVo.getType(),
																	functionVo.getFunctionGroup(),
																	functionVo.convertPageable());
		if(functions.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		List<FunctionVo> functionVos = BeanCopyUtil.copyBeanList(functions.getContent(), FunctionVo.class);
		return new JSGridReturnData<FunctionVo>(functionVos, functions.getTotalElements());
	}

	@Override
	public FunctionVo getFunction(Long id) {
		Optional<Function> function = functionRepository.findById(id);
		if(function.isPresent()) {
			return BeanCopyUtil.copyBean(function.get(), FunctionVo.class);
		}
		return null;
	}	
	
	@Override
	public List<SelectDataVo> findByFunctionType() {
		List<Function> functions = functionRepository.findAll();
		
		List<SelectDataVo> selectFunctionType = functions.stream()
								.filter(f -> "0".equals(f.getFunctionGroup()))
								.map(f -> new SelectDataVo(f.getType(), f.getFunctionShowName()))
								.collect(Collectors.toList());
		return selectFunctionType;
	}

	@Override
	public List<Function> findByEnabled() {
		return functionRepository.findByEnabled("1");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(FunctionVo functionVo, String func) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		String enableStr = functionVo.getEnabled() == null ? "0" : "1";
		functionVo.setEnabled(enableStr);
		
		Function function = BeanCopyUtil.copyBean(functionVo, Function.class);
		Function functionSaveResp = null;
		if(func.equals("新增")) {
    		function.setCreateTime(new Date());
    		function.setCreateUser(username);
    		functionSaveResp = functionRepository.save(function);
    		
    		String functionId = functionSaveResp.getFunctionId().toString();
    		//如果是主選單儲存type為ID
    		//因為上方有做save, 有Transactional可以不用再次save
    		function.setType(functionId);
    	} else {
			if("0".equals(functionVo.getFunctionGroup())) {
    			functionVo.setType(functionVo.getFunctionId().toString());
    		}
    		function.setUpdateTime(new Date());
    		function.setUpdateUser(username);
    		
    		functionSaveResp = functionRepository.save(function);
    	}
		
		if(functionSaveResp == null) {
			return func +"功能資料失敗";
		}
		//更新navBar
		navBarFunctionList();
    	return func +"功能資料成功";
	}
	
	@Override
	public Map<String,List<Function>> navBarFunctionList() {
		//取得啟用中的Function
		List<Function> functionGroups = findByEnabled();
		//分群處理不同類別
		Map<String,List<Function>> functionMap = functionGroups.stream()
				.sorted(comparing(Function::getFunctionGroup)
							.thenComparing(Function::getFunctionSort))
				.collect(groupingBy(Function::getType, LinkedHashMap::new, Collectors.toList()));
		session.setAttribute("functionMap", functionMap);
		return functionMap;
	}
	
	private void checkData(FunctionVo functionVo) {
		if(functionVo.getFunctionName() != null) {
			functionVo.setFunctionName(StringUtil.addPercentage(functionVo.getFunctionName(), 3));
		}
		if(functionVo.getFunctionShowName() != null) {
			functionVo.setFunctionShowName(StringUtil.addPercentage(functionVo.getFunctionShowName(), 3));
		}
		if(functionVo.getFunctionUrl() != null) {
			functionVo.setFunctionUrl(StringUtil.addPercentage(functionVo.getFunctionUrl(), 3));
		}
	}
}
