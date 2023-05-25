package com.vendingmachine.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.entity.Function;
import com.vendingmachine.backend.repositories.AppUserRepository;
import com.vendingmachine.backend.service.FunctionService;
import com.vendingmachine.backend.service.UserFunctionService;
import com.vendingmachine.backend.vo.PermissionVo;
import com.vendingmachine.backend.vo.UserFunctionProjection;

@Controller
@RequestMapping(value = "/permissions")
public class PermissionsController {
	
	@Autowired
	AppUserRepository userRepository;
	
	@Autowired
	FunctionService functionService;
	
	@Autowired
	UserFunctionService userFunctionService;
	
	@GetMapping("/{userId}")
    public String index(Model model, @PathVariable("userId") Long userId) {
		userId = 7L;
		Optional<AppUser> appUser = userRepository.findById(userId);
		if(appUser.isPresent()) {
			model.addAttribute("permissionsUser", appUser.get());
		}
		
		//List<Function> function = functionService.findAll();
		//model.addAttribute("functionsTable", function);
		List<UserFunctionProjection> permissionVo = userFunctionService.queryUserPermission(userId);
		model.addAttribute("functionsTable", permissionVo);
		
		return "permissions";
    }
	
	@GetMapping("/getfuncuser/{userId}")
	@ResponseBody
    public List<UserFunctionProjection> getfuncuser(Model model, @PathVariable("userId") Long userId) {
		List<UserFunctionProjection> permissionVo = userFunctionService.queryUserPermission(userId);
		return permissionVo;
    }
	
}
