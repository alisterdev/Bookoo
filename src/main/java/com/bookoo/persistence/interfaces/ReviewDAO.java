package com.bookoo.persistence.interfaces;

import com.bookoo.data.ReviewBean;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access routine interface for the Book data bean
 * 
 * @author Darrel-Day Guerrero, Jolan Cornevin
 * @version 1.0
 */
public interface ReviewDAO {

	// Client Side (Front End)

	// Add a review
	public int insertReview(ReviewBean review)
			throws SQLException;

	// Update a review
	public int updateReview(ReviewBean review)
			throws SQLException;

	// Remove a review
	public int removeReview(ReviewBean review)
			throws SQLException;

	// Management Side (Back End)
	
	// Fetch all reviews
	public ArrayList<ReviewBean> getAllReviews() throws SQLException;
	
	// Fetch reviews
	public ArrayList<ReviewBean> searchUnapprovedReviews() throws SQLException;
	
	// Approve / Disapprove reviews
	public int updateApprovalStatus(ReviewBean review, boolean isApproved) throws SQLException;

	public ArrayList<ReviewBean> getAllReviewsWithDate(long id) throws SQLException;
	

//	NOT IN SPECS, BUT TO CONSIDER IF TIME ALLOWS US
//
//	Returns average mark for a book
//	
//	public int getTotalMarkForBook(BookBean book) throws SQLException;

}
