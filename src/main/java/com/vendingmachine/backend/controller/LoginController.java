package com.vendingmachine.backend.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vendingmachine.backend.entity.AppUser;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	@Autowired
	HttpSession session;
	
	@GetMapping("/")
    public String index() {
		AppUser appUser = (AppUser) session.getAttribute("appUser");
		if(appUser != null) {
			return "function";
		} else {
			return "login";
		}
    }
	
}
