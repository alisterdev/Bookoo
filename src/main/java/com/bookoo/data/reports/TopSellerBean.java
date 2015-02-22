/**
 * 
 */
package com.bookoo.data.reports;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Anna Rogozin
 * 
 */

public class TopSellerBean implements Serializable {
	
	
	private static final long serialVersionUID = 1897285102772921923L;
	private long bookId;
	private String title;
	private int quantitySold;
	private Calendar date;

	/**
	 * 
	 */
	public TopSellerBean() {
		super();
	}

	public String getDateString() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(date.getTime());
		return formatted;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the bookId
	 */
	public long getBookId() {
		return bookId;
	}

	/**
	 * @param bookId
	 *            the bookId to set
	 */
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the quantitySold
	 */
	public int getQuantitySold() {
		return quantitySold;
	}

	/**
	 * @param quantitySold
	 *            the quantitySold to set
	 */
	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bookId ^ (bookId >>> 32));
		result = prime * result + quantitySold;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopSellerBean other = (TopSellerBean) obj;
		if (bookId != other.bookId)
			return false;
		if (quantitySold != other.quantitySold)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TopSellerBean [bookId=" + bookId + ", title=" + title
				+ ", quantitySold=" + quantitySold + "]";
	}

}
