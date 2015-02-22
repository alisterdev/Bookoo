package com.bookoo.business;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.bookoo.data.SurveyBean;
import com.bookoo.persistence.implementation.SurveyDAOImpl;

/**
 * Survey Bean which handles answer selection, survey approval and question
 * setting.
 * 
 * @author Jolan
 * 
 */
@Named("survey")
@SessionScoped
public class Survey implements Serializable {
	private static final long serialVersionUID = -3858468047526842628L;

	@Inject
	private SurveyBean surveyBean;

	@Inject
	private SurveyDAOImpl sbi;

	private int surveyNumber; // number of the current survey printed.
	private boolean justResponded;
	private int option; // choice of the user for the survey

	public Survey() throws SQLException {
		super();
		option = 0;
		surveyNumber = 0;
		justResponded = false;
	}

	public SurveyBean getSurveyBean() {
		return surveyBean;
	}

	public void setSurveyBean(SurveyBean surveyBean) {
		this.surveyBean = surveyBean;
	}

	/**
	 * used to know is the button "respond" has just been pushed, or not
	 * 
	 * @return
	 */
	public boolean isSurveyCompleted() {
		FacesContext fc = FacesContext.getCurrentInstance();

		boolean bool = false;
		try {
			bool = (boolean) ((HttpServletRequest) fc.getExternalContext()
					.getRequest()).getSession().getAttribute(
					new String("survery" + surveyBean.getSurveyId()));
		} catch (NullPointerException e) {
		}

		return bool;
	}

	public void setSurveyCompleted(boolean surveyCompleted) {
		((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest()).getSession().setAttribute(
				new String("survery" + surveyBean.getSurveyId()), true);
	}

	public int getOption() {

		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}

	/**
	 * When the user click on the submit button to answer the survey
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String answerSurvey() throws SQLException {
		justResponded = true;
		sbi.respondSurvey(option, surveyBean.getSurveyId());
		String surverid = "survery" + surveyBean.getSurveyId();
		((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest()).getSession().setAttribute(
				surverid, true);

		return null;
	}

	/**
	 * load the survey when the user loads a new page
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getSurvey() throws SQLException {

		if (justResponded == false) {

			ArrayList<SurveyBean> list = sbi.getCurrentSurvey();

			if (list.size() == 0) {
				return "failure getting the survey";
			} else if (list.size() == 1) {
				surveyNumber = 0;
				surveyBean = list.get(0);
				return "get survey done";
			} else if (list.size() > 1) {
				surveyNumber = (int) (Math.random() * ((list.size() - 1) + 1));
				surveyBean = list.get(surveyNumber);
			}
		}

		justResponded = false;
		return "get survey done";
	}

	/*
	 * For the get all survey page in the management side
	 */
	public String approuved(SurveyBean sbT) throws SQLException {
		if (sbT.isAvailable() == true) {
			sbT.setAvailable(false);
		} else {
			sbT.setAvailable(true);
		}
		sbi = new SurveyDAOImpl();
		sbi.updateSurvey(sbT);

		return null;
	}

	/**
	 * The 5 following method are used to evaluate if the current survey have an
	 * option 1, 2, 3, 4 and 5 Those method are called by a tag <c:if>, in the
	 * gerenal footer. This tag is called before all others tag, and so, that
	 * why the function getSurvey() is called in the getOption1Setted, in the
	 * aim to load the survey, and use it after.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean getOption1Setted() throws SQLException {
		try {
			getSurvey();
			if (getSurveyBean().getOption1() == null)
				return false;
			else if (getSurveyBean().getOption1().trim().isEmpty()
					|| getSurveyBean().getOption1().equalsIgnoreCase("null"))
				return false;
			else
				return true;
		} catch (Exception e) {

		}
		return false;
	}

	public boolean getOption2Setted() {
		if (getSurveyBean().getOption2() == null) {
			return false;
		} else if (getSurveyBean().getOption2().trim().isEmpty()
				|| getSurveyBean().getOption2().equalsIgnoreCase("null"))
			return false;
		else
			return true;
	}

	public boolean getOption3Setted() {
		if (getSurveyBean().getOption3() == null) {
			return false;
		} else if (getSurveyBean().getOption3().trim().isEmpty()
				|| getSurveyBean().getOption3().equalsIgnoreCase("null"))
			return false;
		else
			return true;
	}

	public boolean getOption4Setted() {
		if (getSurveyBean().getOption4() == null) {
			return false;
		} else if (getSurveyBean().getOption4().trim().isEmpty()
				|| getSurveyBean().getOption4().equalsIgnoreCase("null"))
			return false;
		else
			return true;
	}

	public boolean getOption5Setted() {
		if (getSurveyBean().getOption5() == null) {
			return false;
		} else if (getSurveyBean().getOption5().trim().isEmpty()
				|| getSurveyBean().getOption5().equalsIgnoreCase("null"))

			return false;
		else
			return true;
	}
}