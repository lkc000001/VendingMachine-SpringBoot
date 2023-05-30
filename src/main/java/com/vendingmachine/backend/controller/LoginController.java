package com.vendingmachine.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.repositories.AppUserRepository;
import com.vendingmachine.backend.vo.FunctionVo;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	@Autowired
	AppUserRepository userRepository;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/")
    public String index() {
		AppUser appUser = (AppUser) session.getAttribute("appUser");
		
		if(appUser != null) {
			String functionUrl = "/login/SuccessUrl";
			@SuppressWarnings("unchecked")
			Map<String,List<FunctionVo>> functionMap = (Map<String, List<FunctionVo>>) session.getAttribute("functionMap");
			//取得第1個functionVo
			Optional<FunctionVo> functionVo = functionMap.values().stream()
											   .flatMap(List::stream)
											   .filter(f -> "1".equals(f.getFunctionGroup()))
											   .findFirst();
			
			if(functionVo.isPresent()) {
				functionUrl = functionVo.get().getFunctionUrl();
			}

			return "redirect:.." + functionUrl;
		} else {
			return "login";
		}
    }
}
