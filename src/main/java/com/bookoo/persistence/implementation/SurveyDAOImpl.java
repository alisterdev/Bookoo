package com.bookoo.persistence.implementation;

import com.bookoo.data.SurveyBean;
import com.bookoo.persistence.interfaces.SurveyDAO;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Data Access routine interface for the Survey Data Bean
 * 
 * @author Jolan Cornevin, Darrel-Day Guerrero
 * @version 1.0
 */
@Named("surveyAction")
@RequestScoped
public class SurveyDAOImpl implements SurveyDAO, Serializable {

	private static final long serialVersionUID = -7041778922303987241L;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	public SurveyDAOImpl() {
		super();
	}

	/*
	 * Insert the SurveyBean in parameter in the database
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.SurveyDAO#insertSurvey(com.bookoo.data
	 * .SurveyBean)
	 */
	@Override
	public int insertSurvey(SurveyBean appointment) throws SQLException {
		String preparedQuery = "INSERT INTO SURVEY (QUESTION, OPTION1, OPTION2, OPTION3, OPTION4, OPTION5, RESULT1, RESULT2, RESULT3, RESULT4, RESULT5, AVAILABLE) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		appointment.setResult1(0);
		appointment.setResult2(0);
		appointment.setResult3(0);
		appointment.setResult4(0);
		appointment.setResult5(0);
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setString(1, appointment.getQuestion());
			ps.setString(2, appointment.getOption1());
			ps.setString(3, appointment.getOption2());
			ps.setString(4, appointment.getOption3());
			ps.setString(5, appointment.getOption4());
			ps.setString(6, appointment.getOption5());
			ps.setInt(7, appointment.getResult1());
			ps.setInt(8, appointment.getResult2());
			ps.setInt(9, appointment.getResult3());
			ps.setInt(10, appointment.getResult4());
			ps.setInt(11, appointment.getResult5());
			ps.setBoolean(12, appointment.isAvailable());

			return ps.executeUpdate();
		}
	}

	/*
	 * update all values of an Survey, reach by his Id
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.SurveyDAO#updateSurvey(com.bookoo.data
	 * .SurveyBean)
	 */
	@Override
	public int updateSurvey(SurveyBean appointment) throws SQLException {
		String preparedQuery = "UPDATE SURVEY SET QUESTION = ?, OPTION1 = ?, OPTION2 = ?, OPTION3 = ?, OPTION4 = ?, OPTION5 = ?, RESULT1 = ?, RESULT2 = ?, RESULT3 = ?, RESULT4 = ?, RESULT5 = ?, AVAILABLE = ? WHERE SURVEY_ID = ?";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setString(1, appointment.getQuestion());
			ps.setString(2, appointment.getOption1());
			ps.setString(3, appointment.getOption2());
			ps.setString(4, appointment.getOption3());
			ps.setString(5, appointment.getOption4());
			ps.setString(6, appointment.getOption5());
			ps.setInt(7, appointment.getResult1());
			ps.setInt(8, appointment.getResult2());
			ps.setInt(9, appointment.getResult3());
			ps.setInt(10, appointment.getResult4());
			ps.setInt(11, appointment.getResult5());
			ps.setBoolean(12, appointment.isAvailable());
			ps.setLong(13, appointment.getSurveyId());

			return ps.executeUpdate();
		}
	}

	/*
	 * Get all surveys
	 * 
	 * @see com.bookoo.persistence.interfaces.SurveyDAO#getAllSurveys()
	 */
	@Override
	public ArrayList<SurveyBean> getAllSurveys() throws SQLException {
		ArrayList<SurveyBean> results = new ArrayList<>();

		String preparedQuery = "SELECT * FROM SURVEY";

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					SurveyBean survey = new SurveyBean();

					survey.setSurveyId(resultSet.getLong("SURVEY_ID"));
					survey.setQuestion(resultSet.getString("QUESTION"));
					survey.setOption1(resultSet.getString("OPTION1"));
					survey.setOption2(resultSet.getString("OPTION2"));
					survey.setOption3(resultSet.getString("OPTION3"));
					survey.setOption4(resultSet.getString("OPTION4"));
					survey.setOption5(resultSet.getString("OPTION5"));

					survey.setResult1(resultSet.getInt("RESULT1"));
					survey.setResult2(resultSet.getInt("RESULT2"));
					survey.setResult3(resultSet.getInt("RESULT3"));
					survey.setResult4(resultSet.getInt("RESULT4"));
					survey.setResult5(resultSet.getInt("RESULT5"));

					survey.setAvailable(resultSet.getBoolean("AVAILABLE"));

					results.add(survey);
				}
			}
		}

		return results;
	}

	/*
	 * get all available surveys
	 * 
	 * @see com.bookoo.persistence.interfaces.SurveyDAO#getCurrentSurvey()
	 */
	@Override
	public ArrayList<SurveyBean> getCurrentSurvey() throws SQLException {

		String preparedQuery = "SELECT * FROM SURVEY WHERE AVAILABLE = TRUE";

		ArrayList<SurveyBean> results = new ArrayList<SurveyBean>();

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {

					SurveyBean survey = new SurveyBean();

					survey.setSurveyId(resultSet.getLong("SURVEY_ID"));
					survey.setQuestion(resultSet.getString("QUESTION"));
					survey.setOption1(resultSet.getString("OPTION1"));
					survey.setOption2(resultSet.getString("OPTION2"));
					survey.setOption3(resultSet.getString("OPTION3"));
					survey.setOption4(resultSet.getString("OPTION4"));
					survey.setOption5(resultSet.getString("OPTION5"));
					survey.setResult1(resultSet.getInt("RESULT1"));
					survey.setResult2(resultSet.getInt("RESULT2"));
					survey.setResult3(resultSet.getInt("RESULT3"));
					survey.setResult4(resultSet.getInt("RESULT4"));
					survey.setResult5(resultSet.getInt("RESULT5"));

					survey.setAvailable(resultSet.getBoolean("AVAILABLE"));
					results.add(survey);
				}
			}
		}
		return results;
	}

	/*
	 * get all values of an survey from an id given in parameter
	 * 
	 * @see com.bookoo.persistence.interfaces.SurveyDAO#getSurveyById(long)
	 */
	@Override
	public ArrayList<SurveyBean> getSurveyById(long id) throws SQLException {
		ArrayList<SurveyBean> results = new ArrayList<>();

		String preparedQuery = "SELECT * FROM SURVEY WHERE SURVEY_ID = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setLong(1, id);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					SurveyBean survey = new SurveyBean();

					survey.setSurveyId(resultSet.getLong("SURVEY_ID"));
					survey.setQuestion(resultSet.getString("QUESTION"));
					survey.setOption1(resultSet.getString("OPTION1"));
					survey.setOption2(resultSet.getString("OPTION2"));
					survey.setOption3(resultSet.getString("OPTION3"));
					survey.setOption4(resultSet.getString("OPTION4"));
					survey.setOption5(resultSet.getString("OPTION5"));

					survey.setResult1(resultSet.getInt("RESULT1"));
					survey.setResult2(resultSet.getInt("RESULT2"));
					survey.setResult3(resultSet.getInt("RESULT3"));
					survey.setResult4(resultSet.getInt("RESULT4"));
					survey.setResult5(resultSet.getInt("RESULT5"));

					survey.setAvailable(resultSet.getBoolean("AVAILABLE"));

					results.add(survey);
				}
			}
		}

		return results;
	}

	@Override
	public int respondSurvey(int number, long id) throws SQLException {

		String preparedQuery = "UPDATE SURVEY SET RESULT? = RESULT? + 1"
				+ " WHERE SURVEY_ID = ?";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setLong(1, number);
			pStatement.setLong(2, number);
			pStatement.setLong(3, id);

			return pStatement.executeUpdate();
		}
	}

}