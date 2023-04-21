package com.demo.controller;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.demo.entity.mssql.Function;
import com.demo.repositories.mssql.FunctionRepository;
import com.demo.service.FunctionService;
import com.demo.util.ValidateUtil;

@ControllerAdvice
public class GlobalController {
	
	@Autowired
	FunctionService functionService;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@Autowired
	HttpSession session;
	
	@ModelAttribute(name = "functions")
	public Map<String,List<Function>> getFunctions() {
		@SuppressWarnings("unchecked")
		Map<String,List<Function>> functionMap = (Map<String, List<Function>>) session.getAttribute("functionMap");
		if(functionMap == null) {
			functionMap = functionService.navBarFunctionList();
		}
		return functionMap;
	}
	
	/*@ModelAttribute(name = "appUser")
	public AppUser getAppUser(final Authentication authentication) {
		return userService.getSecurityUser(authentication);
	}*/
	
}
