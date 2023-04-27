package com.vendingmachine.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vendingmachine.backend.entity.Function;
import com.vendingmachine.backend.service.FunctionService;
import com.vendingmachine.backend.vo.FunctionVo;
import com.vendingmachine.backend.vo.JSGridReturnData;
import com.vendingmachine.exception.QueryNoDataException;
import com.vendingmachine.util.BeanCopyUtil;

@Controller
@RequestMapping(value = "/function")
public class FunctionController {
	
	@Autowired
	FunctionService functionService;
	
	private String saveRespMsg;
	
	@GetMapping(path = "/")
    public String index(Model model) {
		model.addAttribute("selectFunctionType", functionService.findByFunctionType());
		model.addAttribute("selectFunction", "function");
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "function";
    }
	
	@PostMapping(path = "/queryFunction", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<FunctionVo>> queryFunction(@RequestBody FunctionVo functionVo) {
		Page<Function> functionPage = functionService.queryFunction(functionVo);
		
		if(functionPage.isEmpty()) {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
		
		List<FunctionVo> functionVos = BeanCopyUtil.copyBeanList(functionPage.getContent(), FunctionVo.class);
    	return ResponseEntity.ok(new JSGridReturnData<FunctionVo>(functionVos, functionPage.getTotalElements()));
    }

	/*@PostMapping(path = "/queryAllFunction", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<Function>> queryAllFunction(@RequestBody JSGridFilter jsGridFilter) {
		JSGridReturnData<Function> functions = functionService.findByAll(jsGridFilter.convertPageable());
    	return ResponseEntity.ok(functions);
    }*/
	
	@GetMapping(path = "/queryEnableFunction")
	@ResponseBody
    public ResponseEntity<JSGridReturnData<Function>> queryEnableFunction() {
		List<Function> functions = functionService.findByEnabled();
    	return ResponseEntity.ok(new JSGridReturnData<Function>(functions, functions.size()));
    }
	
	@GetMapping(path = "/getFunction/{id}")
	@ResponseBody
    public ResponseEntity<FunctionVo> getFunction(@PathVariable("id") Long id) {
		Function function = functionService.getFunction(id);
    	return ResponseEntity.ok(BeanCopyUtil.copyBean(function, FunctionVo.class));
    }
	
	@PostMapping(path = "/save")
	public String addFunction(FunctionVo functionVo) {
		saveRespMsg = functionService.save(functionVo, "新增");
		return "redirect:./";
	}
	
	@PutMapping(path = "/save")
	public String updateFunction(FunctionVo functionVo) {
		saveRespMsg = functionService.save(functionVo, "修改");
		return "redirect:./";
	}
	
}
