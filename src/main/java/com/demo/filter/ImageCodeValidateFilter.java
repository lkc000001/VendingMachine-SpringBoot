package com.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.controller.ValidateCodeController;
import com.demo.entity.ImageCode;
import com.demo.exception.AuthCodeException;
import com.demo.exception.ValidateCodeException;
import com.demo.util.ValidateUtil;

public class ImageCodeValidateFilter extends HttpFilter {

	//private String codeParamter = "authCode"; 
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	    
	@Autowired
	ValidateUtil validateUtil;
	
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ServletWebRequest servletWebRequest = new ServletWebRequest(request);
		ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
		String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");
		if (request.getRequestURI().contains("/login") && codeInRequest != null) {
            try {
            	if (validateUtil.isBlank(codeInRequest)) {
    	            throw new AuthCodeException("驗證碼不能為空", 400);
    	        }

    	        if (validateUtil.isEmpty(codeInSession)) {
    	            throw new ValidateCodeException("驗證碼不存在");
    	        }
    	        
    	        if (codeInSession.isExpire()) {
    	            sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
    	            throw new ValidateCodeException("驗證碼已過期");
    	        }

    	        if (!codeInSession.getCode().equals(codeInRequest)) {
    	            throw new ValidateCodeException("驗證碼錯誤");
    	        }
    	        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
            } catch (ValidateCodeException e) {
            	throw new InternalAuthenticationServiceException(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
	}
}
