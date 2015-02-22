package com.bookoo.business;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.bookoo.data.ClientBean;
import com.bookoo.persistence.implementation.ClientDAOImpl;
import com.bookoo.util.HashPassword;

/**
 * User session bean and methods
 * 
 * @author Alex Ilea, Darrel
 * 
 */
@Named("userSession")
@SessionScoped
public class UserSession implements Serializable {

	private static final long serialVersionUID = -889028616527330536L;

	private boolean isLoggedIn;

	private String inputUsername;
	private String inputPassword;
	private String message;
	private ClientDAOImpl cdi;
	private BigDecimal GST = new BigDecimal(0);
	private BigDecimal PST = new BigDecimal(0);
	private BigDecimal HST = new BigDecimal(0);
	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	@Inject
	private ClientBean client;

	public UserSession() {
		super();
		isLoggedIn = false;
		cdi = new ClientDAOImpl();
		((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest()).getSession().setAttribute(
				"userSession", this);

	}

	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public ClientBean getClient() {
		return client;
	}

	public void setClient(ClientBean client) {
		this.client = client;
	}

	public String getInputUsername() {
		return inputUsername;
	}

	public void setInputUsername(String inputUsername) {
		this.inputUsername = inputUsername;
	}

	public String getInputPassword() {
		return inputPassword;
	}

	public void setInputPassword(String inputPassword) {
		this.inputPassword = inputPassword;
	}

	public BigDecimal getGST() {
		return GST;
	}

	public void setGST(BigDecimal gST) {
		GST = gST;
	}

	public BigDecimal getPST() {
		return PST;
	}

	public void setPST(BigDecimal pST) {
		PST = pST;
	}

	public BigDecimal getHST() {
		return HST;
	}

	public void setHST(BigDecimal hST) {
		HST = hST;
	}

	/**
	 * Accepts user and password from login page and verifies it against login
	 * credentials in database
	 * 
	 * @return
	 * @throws IOException
	 */
	public String checkLogin() throws IOException {

		try {
			client = cdi.searchClient(inputUsername);

			if (client != null) {
				if (HashPassword.validatePassword(inputPassword,
						client.getPassword())) {
					isLoggedIn = true;
					setTaxes();
					ExternalContext ec = FacesContext.getCurrentInstance()
							.getExternalContext();
					ec.redirect(((HttpServletRequest) ec.getRequest())
							.getRequestURI());
				} else {
					message = "Invalid username or password!";
					isLoggedIn = false;
				}

			} else {
				message = "Invalid login!";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Update information of a registered currentUser
	 * 
	 */
	public String updateProfile() throws SQLException {

		try {
			int result = cdi.updateUserProfile(client);

			if (result == 1)
				return "Profile update succeeded";
			else
				return "Profile update failed";

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Destroys the current session and redirect user to the home page.
	 * 
	 * @return
	 */
	public String logout() {
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(true)).invalidate();
		return "logout";
	}

	/**
	 * Determine user's order tax rate based on location.
	 */
	public void setTaxes() {

		if (client.getProvince() == "QC" || client.getProvince() == "SK"
				|| client.getProvince() == "NU" || client.getProvince() == "NT"
				|| client.getProvince() == "AB") {
			GST = new BigDecimal("0.05");
		} else if (client.getProvince() == "PE" || client.getProvince() == "ON"
				|| client.getProvince() == "NS" || client.getProvince() == "NL"
				|| client.getProvince() == "NT") {
			HST = new BigDecimal("0.05");
		} else {
			PST = new BigDecimal("0.05");
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
