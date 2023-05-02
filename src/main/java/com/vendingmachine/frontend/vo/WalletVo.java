package com.vendingmachine.frontend.vo;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vendingmachine.backend.vo.JSGridFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"walletId"}, callSuper = false)
public class WalletVo extends JSGridFilter{

	private Long walletId;
	
	private String walletNo;
	
	private String memberId;
	
	private String memberName;
	
	private Integer amount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	private String createTimeStart;
	
	private String createTimeEnd;

	public WalletVo() {
	}

	public WalletVo(String walletNo, String memberId, Integer amount, Date createTime) {
		this.walletNo = walletNo;
		this.memberId = memberId;
		this.amount = amount;
		this.createTime = createTime;
	}
	
}
