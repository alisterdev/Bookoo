package com.bookoo.data;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Data Bean representing Order details
 * 
 * @author Alex Ilea, CORNEVIN Jolan, Anna Rogozin
 * 
 */
@Named("invoiceDetailsBean")
@RequestScoped
public class InvoiceDetailsBean implements Serializable {

	private static final long serialVersionUID = 8139309442330122378L;
	private long detailsId;
	private long saleId;
	private int quantity;
	private long bookId;
	private BigDecimal bookPrice;
	private String title;
	private String author;
	private String downloadLink;
	private boolean available;

	public InvoiceDetailsBean() {
		super();
	}

	public long getDetailsId() {
		return detailsId;
	}

	public void setDetailsId(long detailsId) {
		this.detailsId = detailsId;
	}

	public long getSaleId() {
		return saleId;
	}

	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public BigDecimal getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(BigDecimal bookPrice) {
		this.bookPrice = bookPrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + (int) (bookId ^ (bookId >>> 32));
		result = prime * result
				+ ((bookPrice == null) ? 0 : bookPrice.hashCode());
		result = prime * result + (int) (detailsId ^ (detailsId >>> 32));
		result = prime * result
				+ ((downloadLink == null) ? 0 : downloadLink.hashCode());
		result = prime * result + quantity;
		result = prime * result + (int) (saleId ^ (saleId >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		InvoiceDetailsBean other = (InvoiceDetailsBean) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (available != other.available)
			return false;
		if (bookId != other.bookId)
			return false;
		if (bookPrice == null) {
			if (other.bookPrice != null)
				return false;
		} else if (!bookPrice.equals(other.bookPrice))
			return false;
		if (detailsId != other.detailsId)
			return false;
		if (downloadLink == null) {
			if (other.downloadLink != null)
				return false;
		} else if (!downloadLink.equals(other.downloadLink))
			return false;
		if (quantity != other.quantity)
			return false;
		if (saleId != other.saleId)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InvoiceDetailsBean [detailsId=" + detailsId + ", saleId="
				+ saleId + ", quantity=" + quantity + ", bookId=" + bookId
				+ ", bookPrice=" + bookPrice + ", title=" + title + ", author="
				+ author + ", downloadLink=" + downloadLink + ", available="
				+ available + "]";
	}

}