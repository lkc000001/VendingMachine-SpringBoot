package com.vendingmachine.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@DynamicUpdate
@Table(name = "[USERFUNCTION]")
public class UserFunction implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USERFUNCTION_ID")
	private Long userFunctionId;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "FUNCTION_ID")
	private Long functionId;
	
	@Column(name = "ENABLED")
	private String enabled;
	
}
