package com.bookoo.business;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.bookoo.data.ReviewBean;
import com.bookoo.persistence.implementation.ReviewDAOImpl;

/**
 * This class handles the add review functionality.
 * 
 */
@Named("addReview")
@SessionScoped
public class AddReview implements Serializable {

	private static final long serialVersionUID = -6738179488010927387L;

	private String review;
	private Integer rating;

	public AddReview() {
		super();
	}

	public String getReview() {
		review = "";
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Integer getRating() {
		return 0;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public ArrayList<ReviewBean> getAllReviews(Long id) throws SQLException {
		ReviewDAOImpl rbi = new ReviewDAOImpl();
		return rbi.getAllReviewsWithDate(id);
	}

	/**
	 * Allows us to add a review. Some parameters are passed to the method,
	 * others are set by default.
	 * 
	 * @param bookId
	 * @param name
	 * @param clientId
	 * @return
	 * @throws SQLException
	 */
	public String comment(Long bookId, String name, Long clientId)
			throws SQLException {

		ReviewDAOImpl rbi = new ReviewDAOImpl();

		ReviewBean rb = new ReviewBean();
		rb.setApprovalStatus(false);
		rb.setDate(new Timestamp(new GregorianCalendar().getTimeInMillis()));
		rb.setClientId(clientId);
		rb.setBookId(bookId);
		rb.setUsername(name);

		if (review.isEmpty())
			return null;
		else
			rb.setReview(review);

		if (rating == null)
			rb.setRating(0);
		else
			rb.setRating(rating);

		rbi.insertReview(rb);

		return null;
	}

	/**
	 * This method handles approving and disapproving reviews.
	 * 
	 * @param tempReview
	 * @return
	 * @throws SQLException
	 */
	public String approved(ReviewBean tempReview) throws SQLException {
		if (tempReview.getApprovalStatus() == true)
			tempReview.setApprovalStatus(false);
		else
			tempReview.setApprovalStatus(true);

		ReviewDAOImpl rbi = new ReviewDAOImpl();
		rbi.updateReview(tempReview);

		return null;
	}

}
