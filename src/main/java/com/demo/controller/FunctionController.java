package com.demo.controller;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

import com.demo.entity.mssql.Function;
import com.demo.service.FunctionService;
import com.demo.vo.FunctionVo;
import com.demo.vo.JSGridFilter;
import com.demo.vo.JSGridReturnData;
import com.demo.vo.SelectDataVo;

@Controller
@RequestMapping(value = "/function")
public class FunctionController {
	
	@Autowired
	FunctionService functionService;
	
	private String saveRespMsg;
	
	@GetMapping(path = "/")
    public String index(Model model) {
		model.addAttribute("selectFunctionType", functionService.findByFunctionType());
		if(saveRespMsg != null) {
			model.addAttribute("saveRespMsg", saveRespMsg);
			saveRespMsg = null;
		}
    	return "function";
    }
	
	@PostMapping(path = "/queryFunction", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<FunctionVo>> queryFunction(@RequestBody FunctionVo functionVo) {
		JSGridReturnData<FunctionVo> functions = functionService.queryFunction(functionVo);
    	return ResponseEntity.ok(functions);
    }

	@PostMapping(path = "/queryAllFunction", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSGridReturnData<Function>> queryAllFunction(@RequestBody JSGridFilter jsGridFilter) {
		JSGridReturnData<Function> functions = functionService.findByAll(jsGridFilter.convertPageable());
    	return ResponseEntity.ok(functions);
    }
	
	@GetMapping(path = "/queryEnableFunction")
	@ResponseBody
    public ResponseEntity<JSGridReturnData<Function>> queryEnableFunction() {
		List<Function> functions = functionService.findByEnabled();
    	return ResponseEntity.ok(new JSGridReturnData<Function>(functions, functions.size()));
    }
	
	@GetMapping(path = "/getFunction/{id}")
	@ResponseBody
    public ResponseEntity<FunctionVo> getFunction(@PathVariable("id") Long id) {
		FunctionVo functionVo = functionService.getFunction(id);
    	return ResponseEntity.ok(functionVo);
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
