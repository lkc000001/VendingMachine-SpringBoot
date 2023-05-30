package com.vendingmachine.backend.controller;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.entity.Function;
import com.vendingmachine.backend.repositories.FunctionRepository;
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
