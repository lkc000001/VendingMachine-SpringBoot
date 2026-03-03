package com.vendingmachine.backend.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vendingmachine.backend.repositories.GroupRepository;
import com.vendingmachine.backend.service.GroupService;
import com.vendingmachine.backend.vo.SelectDataProjection;
import com.vendingmachine.backend.vo.SelectDataVo;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	GroupRepository groupRepository;
//	
//	@Autowired
//	SysPermService sysPermService;
//	
//	@Autowired
//	StringUtil stringUtil;
//	
//	@Autowired
//	HttpSession session;
//	
//	@Autowired
//	DateTimtUtil dateTimtUtil;
//	
//	/**
//	 * 依查詢條件查詢SysGroup
//	 */
//	@Override
//	public JSGridReturnData<SysGroupVo> queryGroup(SysGroupVo sysGroupVo) throws ParseException {
//		checkData(sysGroupVo);
//		List<SysGroupResp> functions = sysGroupRepository.querySysGroupVo(sysGroupVo);
//		int count = 0;
//		if(functions.size() > 0 ) {
//			count = functions.get(0).getTotalCount();
//		}
//		List<SysGroupVo> respFunctions = BeanCopyUtil.copyBeanList(functions, SysGroupVo.class);
//		return JSGridResponse.getResponseData(respFunctions, count);
//	}
	
//	/**
//	 * 依groupItem取得SysGroup
//	 */
//	@Override
//	public SysGroupVo getGroup(Long groupItem) {
//		Optional<SysGroup> resp = sysGroupRepository.findById(groupItem);
//		if(resp.isPresent()) {
//			return BeanCopyUtil.copyBean(resp.get(), SysGroupVo.class);
//		}
//		return null;
//	}	

//	/**
//	 * 新增及修改
//	 * return 0:新增/修改失敗 1: 2:GroupName重復 99:成功
//	 */
//	@Override
//	@Transactional
//	public int save(SysGroupVo sysGroupVo, int func) {
//		SysGroup groupResp = sysGroupRepository.findByGroupName(sysGroupVo.getGroupName());
//		if(groupResp != null) {
//			if(func == 1 || (func == 2 && groupResp.getGroupItem() != sysGroupVo.getGroupItem())) {
//				return 2;
//			}
//		}
//
//		String enableStr = sysGroupVo.getStatus() == null ? "n" : "e";
//		sysGroupVo.setStatus(enableStr);
//		
//		SysGroup sysGroup = BeanCopyUtil.copyBean(sysGroupVo, SysGroup.class);
//		if(func == 1) {
//			sysGroup.setCreateData(session);
//    	} 
//		sysGroup.setModifyData(session);
//		SysGroup groupSaveResp = sysGroupRepository.save(sysGroup);
//		
//		if(groupSaveResp.getGroupItem() == null) {
//			return 0;
//		}
//    	return 99;
//	}
	
	/**
	 * 取得GroupName給前端select選單用
	 */
	@Override
	public List<SelectDataProjection> getGroupNameSelectData() {
		return groupRepository.getGroupNameSelectData();
	}
	
//	/**
//	 * 刪除群組及群組權限
//	 * return 0:刪除群組失敗 1:刪除群組權限失敗 99:成功
//	 */
//	@Override
//	@Transactional
//	public int delete(Long groupItem) {
//		//刪除群組
//		int sysGroupResp = sysGroupRepository.updateByStatus(groupItem);
//		if(sysGroupResp != 1) {
//			return 0;
//		}
//		//刪除權限
//		int sysPermResp = sysPermService.delete(groupItem);
//		if(sysPermResp != 99) {
//			return 1;
//		}
//		return 99;
//	}
	
//	/**
//	 * 驗證查詢條件,加%轉模糊查詢,查詢日期轉換
//	 */
//	private void checkData(SysGroupVo sysGroupVo) throws ParseException {
//		if(sysGroupVo.getGroupName() != null) {
//			sysGroupVo.setGroupName(stringUtil.addPercentage(sysGroupVo.getGroupName(), 3));
//		}
//		
//		//轉換日期格式,加入時分秒
//		Date startDate = sysGroupVo.getStartDate();
//		Date endDate = sysGroupVo.getEndDate();
//		if(startDate != null) {
//			sysGroupVo.setStartDate(dateTimtUtil.changeQueryDate(startDate, 0));
//		}
//		if(endDate != null) {
//			sysGroupVo.setEndDate(dateTimtUtil.changeQueryDate(endDate, 1));
//		}
//	}
}
