package com.vendingmachine.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

public class BeanCopyUtil {
	
	public static <V> V copyBean(Object source, Class<V> clazz) {
		V resule = null;
		try {
			resule = clazz.newInstance();
			BeanUtils.copyProperties(source, resule);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return resule;
	}
	
	public static <O, V> List<V> copyBeanList(List<O> sourceList, Class<V> clazz) {
		return sourceList.stream()
						 .map(o -> copyBean(o, clazz))
						 .collect(Collectors.toList());
	}
}
