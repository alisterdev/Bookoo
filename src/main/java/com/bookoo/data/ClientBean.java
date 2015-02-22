package com.bookoo.data;

import java.io.Serializable;
import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import com.bookoo.persistence.implementation.ClientDAOImpl;

/**
 * Data Bean representing Client
 * 
 * @author Darrel-Day Guerrero, Alex Ilea
 */
@Named("clientBean")
@RequestScoped
public class ClientBean implements Serializable {

	private static final long serialVersionUID = 2990516431692101128L;

	private long id;
	private String username;
	private String password;
	private boolean isManager;
	private String title;
	private String lastName;
	private String firstName;
	private String companyName;
	private String address1;
	private String address2;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private String shipTitle;
	private String shipLastName;
	private String shipFirstName;
	private String shipCompanyName;
	private String shipAddress1;
	private String shipAddress2;
	private String shipCity;
	private String shipProvince;
	private String shipCountry;
	private String shipPostalCode;
	private String phoneNumber;
	private String cellNumber;
	private String email;
	private String lastGenre;

	public ClientBean() {
		super();
		province = "";
		shipProvince = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public void valUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode.toUpperCase();
	}

	public String getShipTitle() {
		return shipTitle;
	}

	public void setShipTitle(String shipTitle) {
		this.shipTitle = shipTitle;
	}

	public String getShipLastName() {
		return shipLastName;
	}

	public void setShipLastName(String shipLastName) {
		this.shipLastName = shipLastName;
	}

	public String getShipFirstName() {
		return shipFirstName;
	}

	public void setShipFirstName(String shipFirstName) {
		this.shipFirstName = shipFirstName;
	}

	public String getShipCompanyName() {
		return shipCompanyName;
	}

	public void setShipCompanyName(String shipCompanyName) {
		this.shipCompanyName = shipCompanyName;
	}

	public String getShipAddress1() {
		return shipAddress1;
	}

	public void setShipAddress1(String shipAddress1) {
		this.shipAddress1 = shipAddress1;
	}

	public String getShipAddress2() {
		return shipAddress2;
	}

	public void setShipAddress2(String shipAddress2) {
		this.shipAddress2 = shipAddress2;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipProvince() {
		return shipProvince;
	}

	public void setShipProvince(String shipProvince) {
		this.shipProvince = shipProvince;
	}

	public String getShipCountry() {
		return shipCountry;
	}

	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public String getShipPostalCode() {
		return shipPostalCode;
	}

	public void setShipPostalCode(String shipPostalCode) {
		if (shipPostalCode != null)
			this.shipPostalCode = shipPostalCode.toUpperCase();
		else
			this.shipPostalCode = shipPostalCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCellNumber() {
		return cellNumber;
	}

	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	public String getLastGenre() {
		return lastGenre;
	}

	public void setLastGenre(String lastGenre) {
		this.lastGenre = lastGenre;
	}

	/***********************
	 * VALIDATOR METHODS
	 **********************/

	/**
	 * Validates username for length and if it exists in Database. Throws
	 * validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 * @throws SQLException
	 */
	public void validateUsername(FacesContext fc, UIComponent c, Object value)
			throws SQLException {

		if (((String) value).length() < 6 || ((String) value).length() > 35) {
			throw new ValidatorException(new FacesMessage(
					"Please use from 6 to 35 characters"));
		} else if (!((String) value).equals(getUsername())) {
			ClientDAOImpl cdi = new ClientDAOImpl();
			boolean isUnique = cdi.checkUniqueUsernameOrEmail((String) value);

			if (!isUnique)
				throw new ValidatorException(
						new FacesMessage(
								(String) value
										+ " is already in use! Please enter a different username."));
		}
	}

	/**
	 * Validates password length to be minimum 8 characters. Throws validator
	 * messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validatePassword(FacesContext fc, UIComponent c, Object value) {

		String pwd = (String) value;

		if (pwd.length() < 8 || pwd.length() > 255) {
			setPassword(pwd);
			throw new ValidatorException(new FacesMessage(
					"Please use at least 8 characters."));
		}
	}

	/**
	 * Validates if password and confirm password fields match. Throws validator
	 * messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validatePasswordMatch(FacesContext fc, UIComponent c,
			Object value) {

		UIInput passInput = (UIInput) c.findComponent("password");
		UIInput confirmPassInput = (UIInput) c.findComponent("confirmpassword");

		String password = (String) passInput.getLocalValue();
		String confirmPassword = (String) value;

		if (password == null || confirmPassword == null) {
			throw new ValidatorException(new FacesMessage(
					"Passwords do not match."));
		} else if (!password.equals(confirmPassword)) {
			passInput.resetValue();
			confirmPassInput.resetValue();
			throw new ValidatorException(new FacesMessage(
					"Passwords do not match."));
		}
	}

	/**
	 * Validates first name to contain only legal characters and be of an
	 * appropriate length. Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateName(FacesContext fc, UIComponent c, Object value) {

		if (!((String) value).matches("^[A-Za-z ,.'-]+$")) {
			throw new ValidatorException(new FacesMessage(
					"Only letters and .'. are allowed!"));
		} else if (((String) value).length() < 3
				|| ((String) value).length() > 34) {
			throw new ValidatorException(new FacesMessage(
					"Please use from 3 to 35 characters!"));
		}

	}

	/**
	 * Validates company name to contain only legal characters and correct
	 * length. Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateCompanyName(FacesContext fc, UIComponent c, Object value) {

		if (!((String) value).matches("^([A-Za-z0-9 ,.'-])*$")) {
			throw new ValidatorException(new FacesMessage(
					"Only letters, numbers and ,.'- are allowed!"));
		} else if (((String) value).length() > 34) {
			throw new ValidatorException(new FacesMessage(
					"Please use a maximum of 35 characters!"));
		}
	}

	/**
	 * Validates phone number to contain only legal characters and correct
	 * length. Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validatePhoneNumber(FacesContext fc, UIComponent c, Object value) {

		if (!((String) value).matches("^([z0-9() -])*$")) {
			throw new ValidatorException(new FacesMessage(
					"Only numbers and ()+- are allowed!"));
		} else if (((String) value).length() > 34) {
			throw new ValidatorException(new FacesMessage(
					"Please use a maximum of 15 characters!"));
		}
	}

	/**
	 * Validates email address to have correct format and check if it is not
	 * already in use Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateEmail(FacesContext fc, UIComponent c, Object value)
			throws SQLException {

		if (!((String) value)
				.matches("[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")) {
			throw new ValidatorException(new FacesMessage(
					"Please enter a valid email!"));
		} else if (!((String) value).equals(getEmail())) {
			ClientDAOImpl cdi = new ClientDAOImpl();
			boolean isUnique = cdi.checkUniqueUsernameOrEmail((String) value);

			if (!isUnique)
				throw new ValidatorException(
						new FacesMessage(
								(String) value
										+ " is already in use! Please enter a different email."));
		}
	}

	/**
	 * Validates address to have an appropriate length Throws validator messages
	 * accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateAddress(FacesContext fc, UIComponent c, Object value) {

		if ((((String) value).length()) != 0
				&& (((String) value).length() < 3 || ((String) value).length() > 35))
			throw new ValidatorException(new FacesMessage(
					"Please use from 3 to 35 characters"));
	}

	/**
	 * Validates address to have an appropriate length and use appropriate
	 * characters Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateCity(FacesContext fc, UIComponent c, Object value) {
		if (!((String) value)
				.matches("^([a-zA-Z]{1,}[\\-\\s]{0,1}[A-Za-z]{0,}){3,30}$")) {
			throw new ValidatorException(new FacesMessage(
					"Please use from 3 to 35 characters! Letters only!"));
		}
	}

	public void validatePostalCode(FacesContext fc, UIComponent c, Object value) {
		if (!((String) value)
				.matches("^[ABCEGHJKLMNPRSTVXYabceghjklmnprstvxy]{1}\\d{1}[A-Za-z]{1} *\\d{1}[A-Za-z]{1}\\d{1}$")) {
			throw new ValidatorException(new FacesMessage(
					"#{msgs_register.errorPostalCode}"));
		}
	}


	/************************************
	 * hashCode(), equals(), toString() *
	 ***********************************/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result
				+ ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result
				+ ((cellNumber == null) ? 0 : cellNumber.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isManager ? 1231 : 1237);
		result = prime * result
				+ ((lastGenre == null) ? 0 : lastGenre.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result
				+ ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result
				+ ((province == null) ? 0 : province.hashCode());
		result = prime * result
				+ ((shipAddress1 == null) ? 0 : shipAddress1.hashCode());
		result = prime * result
				+ ((shipAddress2 == null) ? 0 : shipAddress2.hashCode());
		result = prime * result
				+ ((shipCity == null) ? 0 : shipCity.hashCode());
		result = prime * result
				+ ((shipCompanyName == null) ? 0 : shipCompanyName.hashCode());
		result = prime * result
				+ ((shipCountry == null) ? 0 : shipCountry.hashCode());
		result = prime * result
				+ ((shipFirstName == null) ? 0 : shipFirstName.hashCode());
		result = prime * result
				+ ((shipLastName == null) ? 0 : shipLastName.hashCode());
		result = prime * result
				+ ((shipPostalCode == null) ? 0 : shipPostalCode.hashCode());
		result = prime * result
				+ ((shipProvince == null) ? 0 : shipProvince.hashCode());
		result = prime * result
				+ ((shipTitle == null) ? 0 : shipTitle.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientBean other = (ClientBean) obj;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (address2 == null) {
			if (other.address2 != null)
				return false;
		} else if (!address2.equals(other.address2))
			return false;
		if (cellNumber == null) {
			if (other.cellNumber != null)
				return false;
		} else if (!cellNumber.equals(other.cellNumber))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (isManager != other.isManager)
			return false;
		if (lastGenre == null) {
			if (other.lastGenre != null)
				return false;
		} else if (!lastGenre.equals(other.lastGenre))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (shipAddress1 == null) {
			if (other.shipAddress1 != null)
				return false;
		} else if (!shipAddress1.equals(other.shipAddress1))
			return false;
		if (shipAddress2 == null) {
			if (other.shipAddress2 != null)
				return false;
		} else if (!shipAddress2.equals(other.shipAddress2))
			return false;
		if (shipCity == null) {
			if (other.shipCity != null)
				return false;
		} else if (!shipCity.equals(other.shipCity))
			return false;
		if (shipCompanyName == null) {
			if (other.shipCompanyName != null)
				return false;
		} else if (!shipCompanyName.equals(other.shipCompanyName))
			return false;
		if (shipCountry == null) {
			if (other.shipCountry != null)
				return false;
		} else if (!shipCountry.equals(other.shipCountry))
			return false;
		if (shipFirstName == null) {
			if (other.shipFirstName != null)
				return false;
		} else if (!shipFirstName.equals(other.shipFirstName))
			return false;
		if (shipLastName == null) {
			if (other.shipLastName != null)
				return false;
		} else if (!shipLastName.equals(other.shipLastName))
			return false;
		if (shipPostalCode == null) {
			if (other.shipPostalCode != null)
				return false;
		} else if (!shipPostalCode.equals(other.shipPostalCode))
			return false;
		if (shipProvince == null) {
			if (other.shipProvince != null)
				return false;
		} else if (!shipProvince.equals(other.shipProvince))
			return false;
		if (shipTitle == null) {
			if (other.shipTitle != null)
				return false;
		} else if (!shipTitle.equals(other.shipTitle))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientBean [id=" + id + ", username=" + username
				+ ", password=" + password + ", isManager=" + isManager
				+ ", title=" + title + ", lastName=" + lastName
				+ ", firstName=" + firstName + ", companyName=" + companyName
				+ ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", province=" + province + ", country="
				+ country + ", postalCode=" + postalCode + ", shipTitle="
				+ shipTitle + ", shipLastName=" + shipLastName
				+ ", shipFirstName=" + shipFirstName + ", shipCompanyName="
				+ shipCompanyName + ", shipAddress1=" + shipAddress1
				+ ", shipAddress2=" + shipAddress2 + ", shipCity=" + shipCity
				+ ", shipProvince=" + shipProvince + ", shipCountry="
				+ shipCountry + ", shipPostalCode=" + shipPostalCode
				+ ", phoneNumber=" + phoneNumber + ", cellNumber=" + cellNumber
				+ ", email=" + email + ", lastGenre=" + lastGenre + "]";
	}

}