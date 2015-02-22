package com.bookoo.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Anna Rogozin
 * 
 */
public class CartItemBean implements Serializable {

	private static final long serialVersionUID = -8553447750543171597L;
	private BookBean book;
	private int quantity;
	private BigDecimal total;
	private String typeSelected;

	public CartItemBean() {
		super();
		quantity = 0;
		total = new BigDecimal(0);
	}

	public BookBean getBook() {
		return book;
	}

	public void setBook(BookBean book) {
		this.book = book;
	}

	public long getBookId() {
		return book.getId();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getTypeSelected() {
		return typeSelected;
	}

	public void setTypeSelected(String typeSelected) {
		this.typeSelected = typeSelected;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		total = book.getActualPrice().multiply(new BigDecimal(quantity));
		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		result = prime * result
				+ ((typeSelected == null) ? 0 : typeSelected.hashCode());
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
		CartItemBean other = (CartItemBean) obj;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		if (quantity != other.quantity)
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		if (typeSelected == null) {
			if (other.typeSelected != null)
				return false;
		} else if (!typeSelected.equals(other.typeSelected))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CartItemBean [book=" + book + ", quantity=" + quantity
				+ ", total=" + total + ", typeSelected=" + typeSelected + "]";
	}

}
