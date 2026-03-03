package com.vendingmachine.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vendingmachine.backend.entity.AppUser;

@WebFilter(filterName = "sessionFilter", 
		   urlPatterns = {"/Backend/*", "/Function/*", "/Permissions/*", "/Product/*", "/Purchase/*", "/AppUser/*"})
public class sessionFilter implements Filter {
	
	private static final String ERROR_MSG = "";
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		
		AppUser appUser = (AppUser) session.getAttribute("appUser");

		 // 檢查是否為 AJAX 請求
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

		if(appUser == null) {
			if(isAjax) {
				response.getWriter().write("{\"code\": \"E003\"}");
			} else {
				response.sendRedirect(request.getContextPath() + "/error");
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}
}
