package com.vendingmachine.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.service.AppUserService;
import com.vendingmachine.backend.vo.AppUserVo;
import com.vendingmachine.backend.vo.JSGridReturnData;

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
	
	
	@GetMapping(path = "/getAppUser/{id}")
	@ResponseBody
    public ResponseEntity<AppUserVo> getAppUser(@PathVariable("id") Long id) {
		AppUserVo appUserVo = appUserService.getAppUser(id);
    	return ResponseEntity.ok(appUserVo);
    }
	
	@PostMapping(path = "/save")
	public String addAppUser(AppUserVo appUserVo) {
		System.out.println(appUserVo);
		saveRespMsg = appUserService.save(appUserVo, "新增");
		return "redirect:./";
	}
	
	@PutMapping(path = "/save")
	public String updateAppUser(AppUserVo appUserVo) {
		saveRespMsg = appUserService.save(appUserVo, "修改");
		return "redirect:./";
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long id) {
		return ResponseEntity.ok(appUserService.delete(id));
	}
}
