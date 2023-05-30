package com.vendingmachine.backend.service.impl;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.entity.Function;
import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.repositories.FunctionRepository;
import com.vendingmachine.backend.repositories.UserFunctionRepository;
import com.vendingmachine.backend.service.FunctionService;
import com.vendingmachine.backend.vo.FunctionVo;
import com.vendingmachine.backend.vo.SelectDataVo;
import com.vendingmachine.backend.vo.UserFunctionProjection;
import com.vendingmachine.util.BeanCopyUtil;
import com.vendingmachine.util.StringUtil;

@Service
public class FunctionServiceImpl implements FunctionService {

	@Autowired
	private FunctionRepository functionRepository;

	@Autowired
	UserFunctionRepository userFunctionRepository;
	
	@Autowired
	private StringUtil StringUtil;
	
	@Autowired
	HttpSession session;
	
	@Override
	public Page<Function> queryFunction(FunctionVo functionVo) {
		checkData(functionVo);
		Page<Function> functions = functionRepository.queryFunction(functionVo.getFunctionName(),
																	functionVo.getFunctionShowName(),
																	functionVo.getFunctionUrl(),
																	functionVo.getEnabled(),
																	functionVo.getType(),
																	functionVo.getFunctionGroup(),
																	functionVo.convertPageable());
		return functions;
	}

	@Override
	public Function getFunction(Long id) {
		Optional<Function> function = functionRepository.findById(id);
		if(function.isPresent()) {
			return function.get();
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
    		
    		if("0".equals(functionVo.getFunctionGroup())) {
    			String functionId = functionSaveResp.getFunctionId().toString();
	    		//如果是主選單儲存type為ID
	    		//因為上方有做save, 有Transactional可以不用再次save
	    		function.setType(functionId);
    		}
    		
    	} else {
			if("0".equals(functionVo.getFunctionGroup())) {
				function.setType(functionVo.getFunctionId().toString());
    		}
    		function.setUpdateTime(new Date());
    		function.setUpdateUser(username);
    		
    		functionSaveResp = functionRepository.save(function);
    	}
		
		if(functionSaveResp == null) {
			return func +"功能資料失敗";
		}
		
		//更新navBar
		AppUser appUser = (AppUser) session.getAttribute("appUser");
		if(appUser != null) {
			navBarFunctionList(appUser.getUserId());
		}
		
    	return func +"功能資料成功";
	}
	
	@Override
	public Map<String,List<FunctionVo>> navBarFunctionList(Long userId) {
		

		if(userId == null) {
			return null;
		}
		//取得啟用中的Function
		List<Function> functionGroups = findByEnabled();
		
		List<FunctionVo> functionVos = BeanCopyUtil.copyBeanList(functionGroups, FunctionVo.class);
		
		//取得權限
		List<UserFunction> userPermission = userFunctionRepository.queryUserFunctionByUserIdIsEnable(userId);
		List<Long> permissionFunctionIdList = userPermission.stream()
		        .map(UserFunction::getFunctionId)
		        .collect(Collectors.toList());
		
		functionVos.forEach(u -> {
			Boolean isTrue = permissionFunctionIdList.stream().anyMatch(p-> p == u.getFunctionId());
			if(isTrue) {
				u.setPermissionEnabled("1");
			} else {
				u.setPermissionEnabled("0");
			}
		});
		
		//分群處理不同類別
		Map<String,List<FunctionVo>> functionMap = functionVos.stream()
				.filter(f -> "0".equals(f.getFunctionGroup()) || "1".equals(f.getPermissionEnabled()))
				.sorted(comparing(FunctionVo::getFunctionGroup)
							.thenComparing(FunctionVo::getFunctionSort))
				.collect(groupingBy(FunctionVo::getType, LinkedHashMap::new, Collectors.toList()));

		session.setAttribute("functionMap", functionMap);
		return functionMap;
	}
	
	@Override
	public List<Function> findAll() {
		return functionRepository.findAll();
	}
	
	@Override
	public String getFirstFunctionUrl(Long userId) {
		Map<String,List<FunctionVo>> functionMap = navBarFunctionList(userId);
		Optional<FunctionVo> functionVo = functionMap.values().stream()
				   .flatMap(List::stream)
				   .filter(f -> "1".equals(f.getFunctionGroup()))
				   .findFirst();

		if(functionVo.isPresent()) {
			return functionVo.get().getFunctionUrl();
		}
		return null;
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
