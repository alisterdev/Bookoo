package com.bookoo.util;

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

import com.bookoo.business.UserSession;

/**
 * Login filter that prevent users to access the profile directory if they are
 * not logged in
 * 
 * @author Alex Ilea
 * 
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = { "/faces/profile/*" })
public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		UserSession user = (UserSession) ((HttpServletRequest) request)
				.getSession().getAttribute("userSession");

		if (user == null || !user.getIsLoggedIn()) {
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
