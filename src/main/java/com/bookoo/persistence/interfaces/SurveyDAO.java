package com.bookoo.persistence.interfaces;

import com.bookoo.data.SurveyBean;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access routine interface for the Book data bean
 * 
 * @author Jolan Cornevin, Darrel-Day Guerrero
 * @version 1.0
 */
public interface SurveyDAO{

	public int insertSurvey(SurveyBean appointment) throws SQLException;

	public int updateSurvey(SurveyBean appointment) throws SQLException;

	public ArrayList<SurveyBean> getAllSurveys() throws SQLException;

	public ArrayList<SurveyBean> getCurrentSurvey() throws SQLException;

	public ArrayList<SurveyBean> getSurveyById(long id) throws SQLException;

	public int respondSurvey(int number, long id) throws SQLException;

}
