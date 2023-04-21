package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.service.AppUserService;
import com.demo.vo.AppUserVo;
import com.demo.vo.JSGridReturnData;

@Controller
@RequestMapping(value = "/appUser")
public class AppUserController {
	
	@Autowired
	AppUserService appUserService;
	
	private String saveRespMsg;
	
	@GetMapping("/")
    public String index(Model model) {
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "appuser";
    }
	
	@PostMapping(path = "/queryAppUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<AppUserVo>> queryAppUser(@RequestBody AppUserVo appUserVo) {
		JSGridReturnData<AppUserVo> appUsers = appUserService.queryAppUser(appUserVo);
    	return ResponseEntity.ok(appUsers);
    }
	
}
