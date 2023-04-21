package com.demo.vo;

import javax.persistence.Column;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
public  class JSGridFilter {
	@Transient
	private int pageIndex;
	
	@Transient
	private int pageSize;
	
	@Transient
	private String sortField;
	
	@Transient
	private String sortOrder;
	
    public Pageable convertPageable() {
    	pageIndex = pageIndex <=0 ? 1 : pageIndex;
		pageSize = pageSize <=0 ? 10 : pageSize;
		
		if (sortField == null || "".equals(sortField)) {
			return PageRequest.of(pageIndex-1, pageSize);
		}
		
		sortOrder = (sortOrder == null || "".equals(sortOrder)) ? "ASC" : sortOrder;
		if ("desc".equalsIgnoreCase(sortOrder)) {
			return PageRequest.of(pageIndex-1, pageSize, Sort.by(sortField).descending());
		}
			
		return PageRequest.of(pageIndex-1, pageSize, Sort.by(sortField));
    }
}
