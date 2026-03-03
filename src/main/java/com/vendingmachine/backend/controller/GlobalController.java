package com.vendingmachine.backend.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.service.FunctionService;
import com.vendingmachine.backend.vo.FunctionVo;
import com.vendingmachine.util.ValidateUtil;

@ControllerAdvice
public class GlobalController {
	
	@Autowired
	FunctionService functionService;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@Autowired
	HttpSession session;

	@ModelAttribute(name = "functions")
	public Map<String,List<FunctionVo>> getFunctions() {
		@SuppressWarnings("unchecked")
		Map<String,List<FunctionVo>> functionMap = (Map<String, List<FunctionVo>>) session.getAttribute("functionMap");
		if(functionMap == null) {
			AppUser appUser = (AppUser) session.getAttribute("appUser");
			if(appUser != null) {
				functionMap = functionService.navBarFunctionList(appUser.getUserId());
			}
		}
		return functionMap;
	}
	
}
