package com.bookoo.data;

import java.io.Serializable;
import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import com.bookoo.business.Survey;

/**
 * Data Bean representing Surveys
 * 
 * @author Jolan Cornevin
 * 
 */
@Named("surveyBean")
@RequestScoped
public class SurveyBean implements Serializable {

	private static final long serialVersionUID = 9096718919971042417L;

	private long surveyId;
	private String question;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String option5;
	private int result1;
	private int result2;
	private int result3;
	private int result4;
	private int result5;
	private boolean available;

	public SurveyBean() {
		super();
	}

	public long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String getOption5() {
		return option5;
	}

	public void setOption5(String option5) {
		this.option5 = option5;
	}

	public int getResult1() {
		return result1;
	}

	public void setResult1(int result1) {
		this.result1 = result1;
	}

	public int getResult2() {
		return result2;
	}

	public void setResult2(int result2) {
		this.result2 = result2;
	}

	public int getResult3() {
		return result3;
	}

	public void setResult3(int result3) {
		this.result3 = result3;
	}

	public int getResult4() {
		return result4;
	}

	public void setResult4(int result4) {
		this.result4 = result4;
	}

	public int getResult5() {
		return result5;
	}

	public void setResult5(int result5) {
		this.result5 = result5;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public boolean testOption1() {
		if (option1 == null || option1.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean getTestOption5() {
		if (option5 == null || option5.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	/***********************
	 * VALIDATOR METHODS
	 **********************/

	/**
	 * Validates question length to be minimum 1 and maximum 200 characters.
	 * Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateQuestion(FacesContext fc, UIComponent c, Object value) {

		String qst = (String) value;

		if (qst.length() < 1 || qst.length() > 200) {
			throw new ValidatorException(
					new FacesMessage(
							"Please use at least 1 characters and at most 200 characters."));
		}
	}

	/**
	 * Validates mandatory answer(1, 2 and 3) length to be minimum 1 and maximum
	 * 200 characters. Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateMandatoryAnswer(FacesContext fc, UIComponent c,
			Object value) {

		String asw = (String) value;

		if (asw.length() < 1 || asw.length() > 200) {
			throw new ValidatorException(
					new FacesMessage(
							"Please use at least 1 characters and at most 200 characters."));
		}
	}

	/**
	 * Validates optional answer(4 and 5) length to be minimum 1 and maximum 200
	 * characters. Throws validator messages accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateOptionalAnswer(FacesContext fc, UIComponent c,
			Object value) {

		String asw = (String) value;

		if (asw.length() > 200) {
			throw new ValidatorException(new FacesMessage(
					"Please use at most 200 characters."));
		}
	}

	/**
	 * Validates result, which must be numbers Throws validator messages
	 * accordingly.
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateResult(FacesContext fc, UIComponent c, Object value) {

		try {
			Integer.parseInt((String)value);
		} catch (Exception e) {
			throw new ValidatorException(new FacesMessage(
					"Please use only numbers."));
		}
	}

	/*
	 * For the get all survey page in the management side
	 */
	public String approuved(SurveyBean sbT) throws SQLException {
		Survey survey = new Survey();

		return survey.approuved(sbT);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + ((option1 == null) ? 0 : option1.hashCode());
		result = prime * result + ((option2 == null) ? 0 : option2.hashCode());
		result = prime * result + ((option3 == null) ? 0 : option3.hashCode());
		result = prime * result + ((option4 == null) ? 0 : option4.hashCode());
		result = prime * result + ((option5 == null) ? 0 : option5.hashCode());
		result = prime * result
				+ ((question == null) ? 0 : question.hashCode());
		result = prime * result + result1;
		result = prime * result + result2;
		result = prime * result + result3;
		result = prime * result + result4;
		result = prime * result + result5;
		result = prime * result + (int) (surveyId ^ (surveyId >>> 32));
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
		SurveyBean other = (SurveyBean) obj;
		if (available != other.available)
			return false;
		if (option1 == null) {
			if (other.option1 != null)
				return false;
		} else if (!option1.equals(other.option1))
			return false;
		if (option2 == null) {
			if (other.option2 != null)
				return false;
		} else if (!option2.equals(other.option2))
			return false;
		if (option3 == null) {
			if (other.option3 != null)
				return false;
		} else if (!option3.equals(other.option3))
			return false;
		if (option4 == null) {
			if (other.option4 != null)
				return false;
		} else if (!option4.equals(other.option4))
			return false;
		if (option5 == null) {
			if (other.option5 != null)
				return false;
		} else if (!option5.equals(other.option5))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (result1 != other.result1)
			return false;
		if (result2 != other.result2)
			return false;
		if (result3 != other.result3)
			return false;
		if (result4 != other.result4)
			return false;
		if (result5 != other.result5)
			return false;
		if (surveyId != other.surveyId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SurveyBean [surveyId=" + surveyId + ", question=" + question
				+ ", option1=" + option1 + ", option2=" + option2
				+ ", option3=" + option3 + ", option4=" + option4
				+ ", option5=" + option5 + ", result1=" + result1
				+ ", result2=" + result2 + ", result3=" + result3
				+ ", result4=" + result4 + ", result5=" + result5
				+ ", available=" + available + "]";
	}
}