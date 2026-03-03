package com.vendingmachine.backend.service;

import java.util.List;

import com.vendingmachine.backend.vo.SelectDataProjection;

public interface GroupService {
	
	//JSGridReturnData<SysGroupVo> queryGroup(SysGroupVo groupVo) throws ParseException ;
	
	//SysGroupVo getGroup(Long groupItem);
	
	//int save(SysGroupVo groupVo, int func);
	
	public List<SelectDataProjection> getGroupNameSelectData();
	
	//int delete(Long groupItem);
}
