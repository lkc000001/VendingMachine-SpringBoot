package com.vendingmachine.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.entity.Function;
import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.repositories.AppUserRepository;
import com.vendingmachine.backend.service.FunctionService;
import com.vendingmachine.backend.service.UserFunctionService;
import com.vendingmachine.backend.vo.PermissionVo;
import com.vendingmachine.backend.vo.ProductVo;
import com.vendingmachine.backend.vo.RespDataVo;
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
	
	Long appUserId;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "appUser");
		
		if(appUserId == null || "".equals(appUserId)) {
			return "appuser";
		}
		
		Optional<AppUser> appUser = userRepository.findById(appUserId);
		if(appUser.isPresent()) {
			model.addAttribute("permissionsUser", appUser.get());
		}
		
		List<UserFunctionProjection> permissionVo = userFunctionService.queryUserPermission(appUserId);
		model.addAttribute("functionsTable", permissionVo);
		
		return "permissions";
    }
	
	@GetMapping("/user/{userId}")
    public String getfuncuser(@PathVariable("userId") Long userId) {
		appUserId = userId;
		return "redirect:../";
    }
	
	@PutMapping(path = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RespDataVo> updateUserFunction(@RequestBody List<UserFunction> userFunctions) {
		List<UserFunction> resp = userFunctionService.save(userFunctions);
		return ResponseEntity.ok(new RespDataVo(200, "成功"));
    }
}
