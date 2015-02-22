package com.bookoo.data;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.validation.constraints.Future;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bookoo.util.LuhnCheck;

/**
 * Data Bean JSR 303 for client order payment
 * 
 * @author Darrel-Day Guerrero
 * 
 */
@Named
@SessionScoped
public class PaymentBean implements Serializable {

	private static final long serialVersionUID = -2126504808093181733L;

	@Size(min = 13, message = "com.bookoo.creditCardLength")
	@LuhnCheck
	private String card = "";

	private String fullName = "";

	// CVC 000 - 999
	@Pattern(regexp = "/^[0-9]{3}$/")
	private String cvc = "";

	// Must be a date in the future
	@Future
	private Date date = new Date();

	public void setCard(String newValue) {
		card = newValue;
	}

	public String getCard() {
		return card;
	}

	public void setDate(Date newValue) {
		date = newValue;
	}

	public Date getDate() {
		return date;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
