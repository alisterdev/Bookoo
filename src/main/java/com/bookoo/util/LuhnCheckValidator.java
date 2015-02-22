package com.bookoo.util;

import java.io.Serializable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Carries out the validation for credit cards.
 * 
 * @author Darrel-Day Guerrero
 */
public class LuhnCheckValidator implements
		ConstraintValidator<LuhnCheck, String>, Serializable {

	private static final long serialVersionUID = 8734074771755137462L;

	public void initialize(LuhnCheck constraintAnnotation) {
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		return luhnCheck(value.replaceAll("\\D", "")); // remove non-digits
	}

	/**
	 * Luhn Check validation algorithm
	 * 
	 * @param cardNumber
	 * @return
	 */
	private static boolean luhnCheck(String cardNumber) {
		int sum = 0;

		for (int i = cardNumber.length() - 1; i >= 0; i -= 2) {
			sum += Integer.parseInt(cardNumber.substring(i, i + 1));
			if (i > 0) {
				int d = 2 * Integer.parseInt(cardNumber.substring(i - 1, i));
				if (d > 9)
					d -= 9;
				sum += d;
			}
		}

		return sum % 10 == 0;
	}
}
