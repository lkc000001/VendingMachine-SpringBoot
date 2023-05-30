package com.vendingmachine.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vendingmachine.backend.controller.ValidateCodeController;
import com.vendingmachine.backend.entity.ImageCode;
import com.vendingmachine.exception.ValidateCodeException;
import com.vendingmachine.util.ValidateUtil;

public class ImageCodeValidateFilter extends OncePerRequestFilter {

	@Autowired
	AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	ValidateUtil validateUtil;
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ServletWebRequest servletWebRequest = new ServletWebRequest(request);
		if (request.getRequestURI().contains("/login") && "POST".equals(request.getMethod())) {
			try {
				validateCode(servletWebRequest);
			} catch (ValidateCodeException e) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				return;
			}
		}
        filterChain.doFilter(request, response);
	}
	
	private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
		ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
		String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");

    	if (validateUtil.isBlank(codeInRequest)) {
            throw new ValidateCodeException("驗證碼不能為空");
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
	}
}
