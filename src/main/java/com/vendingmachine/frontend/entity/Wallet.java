package com.vendingmachine.frontend.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@DynamicUpdate
@Table(name = "WALLET")	
public class Wallet implements Serializable {

	@Id
	@SequenceGenerator(name = "WALLET_SEQ", sequenceName = "WALLET_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WALLET_SEQ")
	@Column(name = "WALLET_ID")
	private Long walletId;
	
	@Column(name = "WALLET_NO")
	private String walletNo;
	
	@Column(name = "MEMBER_ID")
	private String memberId;
	
	@Column(name = "AMOUNT")
	private Integer amount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	
	@Column(name = "UPDATE_USER")
	private String updateUser;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
}
