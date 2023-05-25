package com.vendingmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan
public class VendingMachineSpringBootApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(VendingMachineSpringBootApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(VendingMachineSpringBootApplication.class);
	}
	
}
