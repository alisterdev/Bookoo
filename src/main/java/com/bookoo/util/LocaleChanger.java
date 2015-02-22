package com.bookoo.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Locale changer class that is responsible for keeping the language chosen by
 * user consistent across all pages
 * 
 * @author Alex Ilea
 */
@Named
@SessionScoped
public class LocaleChanger implements Serializable {

	private static final long serialVersionUID = 1L;

	private Locale locale;

	@PostConstruct
	public void init() {
		locale = FacesContext.getCurrentInstance().getApplication()
				.getDefaultLocale();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Changes the locale on the web site globally.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String changeLocale() throws IOException {

		ResourceBundle.clearCache();
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		String language = context.getRequestParameterMap().get("locale");

		if (language.equals("fr"))
			setLocale(Locale.CANADA_FRENCH);
		else
			setLocale(Locale.CANADA);

		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);

		return null;
	}

}
