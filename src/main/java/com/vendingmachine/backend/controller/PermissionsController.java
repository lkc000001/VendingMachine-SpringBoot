package com.vendingmachine.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.repositories.AppUserRepository;
import com.vendingmachine.backend.service.FunctionService;
import com.vendingmachine.backend.service.UserFunctionService;
import com.vendingmachine.backend.vo.PermissionsAppUserProjection;
import com.vendingmachine.backend.vo.RespDataVo;
import com.vendingmachine.backend.vo.UserFunctionProjection;

@Controller
@RequestMapping(value = "/Permissions")
public class PermissionsController {
	
	@Autowired
	AppUserRepository userRepository;
	
	@Autowired
	FunctionService functionService;
	
	@Autowired
	UserFunctionService userFunctionService;
	
	Long appUserId = null;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "AppUser");
		
		if(appUserId == null) {
			return "appuser";
		}
		
		PermissionsAppUserProjection appUser = userRepository.getAppUserById(appUserId);
		System.out.println("appUser: " + appUser.getUserId());
		model.addAttribute("permissionsUser", appUser);
		
		List<UserFunctionProjection> permissionVo = userFunctionService.queryUserPermission(appUserId);
		model.addAttribute("functionsTable", permissionVo);
		appUserId = null;
		 
		return "permissions";
    }
	
	@GetMapping("/user/{userId}")
    public String getfuncuser(@PathVariable("userId") Long userId) {
		appUserId = userId;
		return "redirect:../";
    }
	
	@PutMapping(path = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> updateUserFunction(@RequestBody List<UserFunction> userFunctions) {
		userFunctionService.save(userFunctions);
		return ResponseEntity.ok(new RespDataVo(200, "修改成功"));
    }
}
