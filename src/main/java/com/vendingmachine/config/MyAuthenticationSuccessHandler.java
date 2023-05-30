package com.vendingmachine.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.repositories.AppUserRepository;
import com.vendingmachine.backend.service.FunctionService;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	AppUserRepository userRepository;
	
	@Autowired
	FunctionService functionService;
	
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public MyAuthenticationSuccessHandler() {
        super();
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
    	handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
    	String functionUrl = "/login/";
    	String username = authentication.getName();
    	AppUser appUser = userRepository.findByAccount(username);
		if(appUser != null) {
			functionUrl = functionService.getFirstFunctionUrl(appUser.getUserId());
			System.out.println("functionUrl:" + functionUrl);
			request.getSession().setAttribute("appUser", appUser);
			if(functionUrl==null) {
				functionUrl = "/error/";
			}
		} 
        redirectStrategy.sendRedirect(request, response, functionUrl);
    }

    /**
     * Removes temporary authentication-related data which may have been stored in the session
     * during the authentication process.
     */
    protected final void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
