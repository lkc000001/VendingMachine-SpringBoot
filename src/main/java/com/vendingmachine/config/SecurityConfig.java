package com.vendingmachine.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vendingmachine.filter.ImageCodeValidateFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	PasswordEncoder password() {
		return new BCryptPasswordEncoder(); // 將密碼進行加密(每次加密資料並不會相同) 強度可以輸入 4~31
	}
	
	@Bean
    public ImageCodeValidateFilter imageCodeValidateFilter() {
        return new ImageCodeValidateFilter();
    }
    
    // 配置身份驗證
 	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth.userDetailsService(userDetailsService).passwordEncoder(password());
 	}
 	
    // 配置 HTTP 安全性
 	@Override
 	protected void configure(HttpSecurity http) throws Exception {
 		String[] authenticatedUrl = {"/function/**","/product/**"};
 		http.authorizeRequests()
 			.anyRequest().permitAll() // 其他請求皆開放
 			//.anyRequest().authenticated() // 所有請求都要驗證product
 			//.antMatchers("/function/**").authenticated()
 			//.antMatchers("/product/**").hasRole("product")
 			.and()
 			.formLogin()
 			.loginProcessingUrl("/loginAction")
 			.loginPage("/login/")
 			.defaultSuccessUrl("/product/")
 			.failureUrl("/login/?error=true")
 			.and().csrf().disable();
 			
 		http.logout()
 			.logoutUrl("/logout")
 			.deleteCookies("JSESSIONID")
 			.logoutSuccessUrl("/login/");
 		
 		http.addFilterBefore(imageCodeValidateFilter(), UsernamePasswordAuthenticationFilter.class);
	 
 	}
 	
 	// 配置網路安全
 	@Override
 	public void configure(WebSecurity web) throws Exception {
 		System.out.println("configure(WebSecurity web)");
 		// 不需要驗證的路徑
 		web.ignoring().antMatchers("/", "/login/**", "/component/**", "/images/**", "/code/image");
 	}
}
