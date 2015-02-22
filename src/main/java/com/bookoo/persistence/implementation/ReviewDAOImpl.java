package com.bookoo.persistence.implementation;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.bookoo.data.ReviewBean;
import com.bookoo.persistence.interfaces.ReviewDAO;

/**
 * Data Access Object for client reviews on books
 * 
 * @author Jolan Cornevin, Alex Ilea, Darrel-Day Guerrero
 * @version 1.0
 */

@Named("reviewAction")
@RequestScoped
public class ReviewDAOImpl implements ReviewDAO, Serializable {

	private static final long serialVersionUID = 1077624360042217348L;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	public ReviewDAOImpl() {
		super();
	}

	// Add a review
	@Override
	public int insertReview(ReviewBean review) throws SQLException {
		String preparedQuery = "INSERT INTO REVIEW (BOOK_ID, REVIEW_DATE, CLIENT_ID, CLIENT_NAME, RATING, REVIEW, APPROVAL_STATUS) values (?, ?, ?, ?, ?, ?, ?)";

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

			ps.setLong(1, review.getBookId());
			ps.setTimestamp(2, review.getDate());
			ps.setLong(3, review.getClientId());
			ps.setString(4, review.getUsername());
			ps.setInt(5, review.getRating());
			ps.setString(6, review.getReview());
			ps.setBoolean(7, review.getApprovalStatus());

			return ps.executeUpdate();
		}

	}

	// Update a review
	@Override
	public int updateReview(ReviewBean review) throws SQLException {
		String preparedQuery = "UPDATE REVIEW SET BOOK_ID = ?, REVIEW_DATE = ?, CLIENT_ID = ?, CLIENT_NAME = ?, RATING = ?, REVIEW = ?, APPROVAL_STATUS = ? WHERE REVIEW_ID = ?";

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

			ps.setLong(1, review.getBookId());
			ps.setTimestamp(2, review.getDate());
			ps.setLong(3, review.getClientId());
			ps.setString(4, review.getUsername());
			ps.setInt(5, review.getRating());
			ps.setString(6, review.getReview());
			ps.setBoolean(7, review.getApprovalStatus());
			ps.setLong(8, review.getId());

			return ps.executeUpdate();
		}
	}

	// Remove a review
	@Override
	public int removeReview(ReviewBean review) throws SQLException {
		String preparedQuery = "UPDATE REVIEW SET APPROVAL_STATUS = FALSE WHERE REVIEW_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setLong(1, review.getId());

			return ps.executeUpdate();
		}
	}

	// Management Side (Back End)
	// Fetch all reviews
	@Override
	public ArrayList<ReviewBean> getAllReviews() throws SQLException {

		ArrayList<ReviewBean> results = new ArrayList<ReviewBean>();

		String preparedQuery = "SELECT * FROM REVIEW";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);
				ResultSet resultSet = pStatement.executeQuery()) {
			while (resultSet.next()) {

				// Process results
				ReviewBean review = new ReviewBean();
				review.setId(resultSet.getLong("REVIEW_ID"));
				review.setBookId(resultSet.getInt("BOOK_ID"));
				review.setDate(resultSet.getTimestamp("REVIEW_DATE"));
				review.setClientId(resultSet.getInt("CLIENT_ID"));
				review.setUsername(resultSet.getString("CLIENT_NAME"));
				review.setRating(resultSet.getInt("RATING"));
				review.setReview(resultSet.getString("REVIEW"));
				review.setApprovalStatus(resultSet
						.getBoolean("APPROVAL_STATUS"));

				results.add(review);
			}
			return results;
		}
	}

	@Override
	public ArrayList<ReviewBean> getAllReviewsWithDate(long id)
			throws SQLException {
		ArrayList<ReviewBean> results = new ArrayList<ReviewBean>();

		String preparedQuery = "SELECT * FROM REVIEW WHERE APPROVAL_STATUS = TRUE AND BOOK_ID = ? ORDER BY REVIEW_DATE DESC";

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

			pStatement.setLong(1, id);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {

					// Process results
					ReviewBean review = new ReviewBean();
					review.setId(resultSet.getLong("REVIEW_ID"));
					review.setBookId(resultSet.getInt("BOOK_ID"));
					review.setDate(resultSet.getTimestamp("REVIEW_DATE"));
					review.setClientId(resultSet.getInt("CLIENT_ID"));
					review.setUsername(resultSet.getString("CLIENT_NAME"));
					review.setRating(resultSet.getInt("RATING"));
					review.setReview(resultSet.getString("REVIEW"));
					review.setApprovalStatus(resultSet
							.getBoolean("APPROVAL_STATUS"));

					results.add(review);
				}
			}
		}

		return results;
	}

	/*
	 * get all review where APPROVAL_STATUS is false
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.ReviewDAO#searchUnapprovedReviews()
	 */
	@Override
	public ArrayList<ReviewBean> searchUnapprovedReviews() throws SQLException {

		ArrayList<ReviewBean> results = new ArrayList<>();

		String preparedQuery = "SELECT * FROM REVIEW WHERE APPROVAL_STATUS=FALSE";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					ReviewBean review = new ReviewBean();
					review.setId(resultSet.getLong("REVIEW_ID"));
					review.setBookId(resultSet.getInt("BOOK_ID"));
					review.setDate(resultSet.getTimestamp("REVIEW_DATE"));
					review.setClientId(resultSet.getInt("CLIENT_ID"));
					review.setUsername(resultSet.getString("CLIENT_NAME"));
					review.setRating(resultSet.getInt("RATING"));
					review.setReview(resultSet.getString("REVIEW"));
					review.setApprovalStatus(resultSet
							.getBoolean("APPROVAL_STATUS"));

					results.add(review);
				}
			}
		}

		return results;
	}

	/*
	 * Update the APPROVAL_STATUS of a review.
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.ReviewDAO#updateApprovalStatus(com.
	 * bookoo.data.ReviewBean, boolean)
	 */
	@Override
	public int updateApprovalStatus(ReviewBean review, boolean isApproved)
			throws SQLException {
		String preparedQuery = "UPDATE REVIEW SET APPROVAL_STATUS = ? WHERE REVIEW_ID = ? ";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setBoolean(1, isApproved);
			ps.setLong(2, review.getId());

			return ps.executeUpdate();
		}
	}

}
