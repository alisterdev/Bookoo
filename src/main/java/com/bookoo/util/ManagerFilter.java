package com.bookoo.util;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebFilter;

import com.bookoo.business.UserSession;

/**
 * Filter checks if LoginBean has loginIn property set to true. If it is not set
 * then request is being redirected to the login.xhml page.
 * From: itcuties - http://www.itcuties.com/j2ee/jsf-2-login-filter-example/
 * 
 * @author Alex Ilea, Darrel-Day Guerrero
 * 
 */
@WebFilter(filterName = "ManagerFilter", urlPatterns = { "/faces/manager/*" })
public class ManagerFilter implements Filter { 


	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		

		UserSession user = (UserSession) ((HttpServletRequest) request)
				.getSession().getAttribute("userSession");
		
		if (user == null || !user.getClient().isManager()) {
			String contextPath = ((HttpServletRequest) request)
					.getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath
					+ "/faces/home.xhtml");
		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		// Nothing to do here!
	}

	public void destroy() {
		// Nothing to do here!
	}

}
