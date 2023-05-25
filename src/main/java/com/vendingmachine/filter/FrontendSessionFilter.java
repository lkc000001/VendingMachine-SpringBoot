package com.vendingmachine.filter;

import java.io.IOException;
import java.io.PrintWriter;

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

import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.frontend.vo.RespDataVo;

//@WebFilter(filterName = "FrontendSessionFilter", 
//		   urlPatterns = {"/member/*", "/memberorder/*", "/shopping/*", "/wallet/*"})
public class FrontendSessionFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		AppUser frontendUser = (AppUser) session.getAttribute("frontendUser");
		
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setHeader("Access-Control-Allow-Origin", "Origin, http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
		if(!uri.contains("addMember") && frontendUser == null) {
			
			// 設定Response編碼為UTF-8
            response.setCharacterEncoding("UTF-8");
            // 設定Response的ContentType
            response.setContentType("application/json; charset=utf-8");
            // 驗證失敗，設定Response的狀態為401
            response.setStatus(401);
 
            // 將Object轉換成JSON字串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRspBody = objectMapper.writeValueAsString(new RespDataVo(0401, "尚未登入", 0));
 
            // 宣告PrintWriter來回傳內容
            PrintWriter printWriter = response.getWriter();
            printWriter.append(jsonRspBody);
            printWriter.close();
			
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
