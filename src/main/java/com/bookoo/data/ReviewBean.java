package com.bookoo.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 * Bean representing Reviews
 * 
 * @author Darrel-Day Guerrero, Alex Ilea
 * 
 */

@Named("reviewBean")
@RequestScoped
public class ReviewBean implements Serializable {

	private static final long serialVersionUID = -4226033630562439283L;

	private long id;
	private long bookId;
	private Timestamp date;
	private long clientId;
	private String username;
	private int rating;
	private String review;
	private Boolean approvalStatus;

	public ReviewBean() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookid) {
		bookId = bookid;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientid) {
		clientId = clientid;
	}

	public Timestamp getDate() {
		return date;
	}

	public String getFormattedDate() {
		return new SimpleDateFormat("MMMM dd, yyyy").format(date);
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Boolean getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Boolean approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * Custom validator for reviews
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateReview(FacesContext fc, UIComponent c, Object value) {

		if (!(((String) value).matches("[^;|/\"<>]+"))) {
			throw new ValidatorException(
					new FacesMessage(
							"Review should not contain any of these symbols: \" $ ^ ! / ; < >"));
		} else if (((String) value).trim().length() > 750) {
			throw new ValidatorException(new FacesMessage(
					"Your review size must be under 750 character"));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approvalStatus == null) ? 0 : approvalStatus.hashCode());
		result = prime * result + (int) (bookId ^ (bookId >>> 32));
		result = prime * result + (int) (clientId ^ (clientId >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		result = prime * result + rating;
		result = prime * result + ((review == null) ? 0 : review.hashCode());
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
		ReviewBean other = (ReviewBean) obj;
		if (approvalStatus == null) {
			if (other.approvalStatus != null)
				return false;
		} else if (!approvalStatus.equals(other.approvalStatus))
			return false;
		if (bookId != other.bookId)
			return false;
		if (clientId != other.clientId)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (rating != other.rating)
			return false;
		if (review == null) {
			if (other.review != null)
				return false;
		} else if (!review.equals(other.review))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReviewBean [id=" + id + ", bookId=" + bookId + ", date=" + date
				+ ", clientId=" + clientId + ", nameClient=" + username
				+ ", rating=" + rating + ", review=" + review
				+ ", approvalStatus=" + approvalStatus + "]";
	}

}
