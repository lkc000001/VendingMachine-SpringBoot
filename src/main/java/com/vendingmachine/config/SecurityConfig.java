package com.vendingmachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vendingmachine.backend.service.impl.UserDetailsServiceImpl;
import com.vendingmachine.filter.ImageCodeValidateFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	PasswordEncoder password() {
		return new BCryptPasswordEncoder(); // 將密碼進行加密(每次加密資料並不會相同) 強度可以輸入 4~31
	}
	
	//驗證碼
	@Bean
    public ImageCodeValidateFilter imageCodeValidateFilter() {
        return new ImageCodeValidateFilter();
    }
    
    // 身份驗證
 	@Bean
    public UserDetailsServiceImpl springUserService() {
        return new UserDetailsServiceImpl();
    }
 	//驗證成功
 	@Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler();
    }
 	//驗證失敗
 	@Bean
    public MyAuthenticationFailureHandler myAuthenticationFailureHandler(){
        return new MyAuthenticationFailureHandler();
    }

	//不需要驗證的URL
	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return web-> web
				.ignoring()
				.antMatchers("/", "/login/**", "/component/**", "/images/**", "/code/image", "/frontend/**");
	}
      
	// 配置 HTTP 安全性
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	 
    	 //登入
    	 http.formLogin()
			 .loginProcessingUrl("/loginAction")
			 .loginPage("/login/")
			 .successHandler(myAuthenticationSuccessHandler())
			 .failureHandler(myAuthenticationFailureHandler());
    	 
    	 //驗證碼
    	 http.addFilterBefore(imageCodeValidateFilter(), UsernamePasswordAuthenticationFilter.class);
    	 
    	 //權限
    	 http.authorizeRequests()
     		 //.anyRequest().authenticated() // 所有請求都要驗證
    	 	 .antMatchers("/frontend/**").permitAll()
			 .antMatchers("/appUser/**").hasRole("appUser")
			 .antMatchers("/function/**").hasRole("function")
			 .antMatchers("/product/**").hasRole("product")
			 .antMatchers("/purchase/**").hasRole("purchase")
			 .antMatchers("/backend/member/**").hasRole("member")
			 .antMatchers("/backend/memberorder/**").hasRole("memberorder")
 			 .and()
			 .userDetailsService(springUserService())
			 .csrf().disable();
    	 
    	 //登出
    	 http.logout()
    	 	 .logoutUrl("/logout")
			 .deleteCookies("JSESSIONID")
			 .logoutSuccessUrl("/login/");
    	 
    	 return http.build();
	}
}
