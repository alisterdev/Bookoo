package com.bookoo.util;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Jolan Cornevin
 *
 */
@Named("cookieHandler")
@RequestScoped
public class CookieHandler implements Serializable {

	private static final long serialVersionUID = 2216624928374902288L;

	public boolean isCookieExist() {
		FacesContext context = FacesContext.getCurrentInstance();
		// Retrieve a specific cookie
		Object my_cookie = context.getExternalContext().getRequestCookieMap()
				.get("Genre");
		if (my_cookie == null) {
			return false;
		}
		return true;
	}

	/**
	 * Read cookie genre
	 * 
	 * @return
	 */
	public String checkGenreCookies() {
		FacesContext context = FacesContext.getCurrentInstance();

		// Retrieve a specific cookie
		Object my_cookie = context.getExternalContext().getRequestCookieMap()
				.get("Genre");
		if (my_cookie != null) {
			return (((Cookie) my_cookie).getValue());
		}

		return null;
	}

	/**
	 * Edit or write genre cookie
	 * 
	 * @param genre
	 *            Cookie for genre
	 */
	public void writeGenreCookie(String genre) {
		FacesContext context = FacesContext.getCurrentInstance();

		// Retrieve a genre cookie
		Cookie my_cookie = (Cookie) context.getExternalContext()
				.getRequestCookieMap().get("Genre");

		// Set value of cookie if exist
		if (my_cookie != null) {
			my_cookie.setValue(genre);
		} else {
			my_cookie = new Cookie("Genre", genre);
			my_cookie.setMaxAge(-1);
		}
		HttpServletResponse temp = (HttpServletResponse) (context
				.getExternalContext().getResponse());
		temp.addCookie(my_cookie);
	}
}
