package com.bookoo.data.reports;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Sihem Adnani
 * 
 */

public class ZeroSalesBean implements Serializable {

	private static final long serialVersionUID = -6074000664989592866L;

	private long id;
	private String isbn13;
	private String isbn10;
	private String title;
	private String author;
	private String publisher;
	private int pages;
	private String genre;
	private String cover;
	private int bookType;
	private int bookFormat;
	private String downloadLink;
	private int copies;
	private BigDecimal wholesalePrice;
	private BigDecimal listPrice;
	private BigDecimal salePrice;
	private BigDecimal actualPrice;
	private Double weight;
	private String dimensions;
	private Timestamp dateAdded;
	private boolean isAvailable;
	private Calendar date;

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

	public ZeroSalesBean() {
		super();
	}

	/**
	 * Custom method needed to trim title when they are too long to display.
	 * 
	 * @return String title
	 */
	public String getTrimmedTitle() {

		if (title.length() > 20)
			return title.substring(0, 20) + "...";
		else
			return title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
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

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	/**
	 * get the book type, and cast it to the associate string
	 * 
	 * @return
	 */
	public String getStringBookType() {

		switch (bookType) {
		case 0:
			return "Hard cover";
		case 1:
			return "Hard cover and Ebook";
		case 2:
			return "Soft cover";
		case 3:
			return "Soft cover and Ebook";
		case 4:
			return "Ebook";
		}

		return null;
	}

	public int getBookType() {
		return bookType;
	}

	public void setBookType(int bookType) {
		this.bookType = bookType;
	}

	public int getBookFormat() {
		return bookFormat;
	}

	public void setBookFormat(int bookFormat) {
		this.bookFormat = bookFormat;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public BigDecimal getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(BigDecimal wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getActualPrice() {
		if (salePrice.compareTo(new BigDecimal(0)) != 0)
			actualPrice = salePrice;
		else
			actualPrice = listPrice;
		return actualPrice;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getDimensions() {
		return dimensions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualPrice == null) ? 0 : actualPrice.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + bookFormat;
		result = prime * result + bookType;
		result = prime * result + copies;
		result = prime * result + ((cover == null) ? 0 : cover.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = prime * result
				+ ((dimensions == null) ? 0 : dimensions.hashCode());
		result = prime * result
				+ ((downloadLink == null) ? 0 : downloadLink.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isAvailable ? 1231 : 1237);
		result = prime * result + ((isbn10 == null) ? 0 : isbn10.hashCode());
		result = prime * result + ((isbn13 == null) ? 0 : isbn13.hashCode());
		result = prime * result
				+ ((listPrice == null) ? 0 : listPrice.hashCode());
		result = prime * result + pages;
		result = prime * result
				+ ((publisher == null) ? 0 : publisher.hashCode());
		result = prime * result
				+ ((salePrice == null) ? 0 : salePrice.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		result = prime * result
				+ ((wholesalePrice == null) ? 0 : wholesalePrice.hashCode());
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
		ZeroSalesBean other = (ZeroSalesBean) obj;
		if (actualPrice == null) {
			if (other.actualPrice != null)
				return false;
		} else if (!actualPrice.equals(other.actualPrice))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (bookFormat != other.bookFormat)
			return false;
		if (bookType != other.bookType)
			return false;
		if (copies != other.copies)
			return false;
		if (cover == null) {
			if (other.cover != null)
				return false;
		} else if (!cover.equals(other.cover))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (dateAdded == null) {
			if (other.dateAdded != null)
				return false;
		} else if (!dateAdded.equals(other.dateAdded))
			return false;
		if (dimensions == null) {
			if (other.dimensions != null)
				return false;
		} else if (!dimensions.equals(other.dimensions))
			return false;
		if (downloadLink == null) {
			if (other.downloadLink != null)
				return false;
		} else if (!downloadLink.equals(other.downloadLink))
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (id != other.id)
			return false;
		if (isAvailable != other.isAvailable)
			return false;
		if (isbn10 == null) {
			if (other.isbn10 != null)
				return false;
		} else if (!isbn10.equals(other.isbn10))
			return false;
		if (isbn13 == null) {
			if (other.isbn13 != null)
				return false;
		} else if (!isbn13.equals(other.isbn13))
			return false;
		if (listPrice == null) {
			if (other.listPrice != null)
				return false;
		} else if (!listPrice.equals(other.listPrice))
			return false;
		if (pages != other.pages)
			return false;
		if (publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		if (salePrice == null) {
			if (other.salePrice != null)
				return false;
		} else if (!salePrice.equals(other.salePrice))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		if (wholesalePrice == null) {
			if (other.wholesalePrice != null)
				return false;
		} else if (!wholesalePrice.equals(other.wholesalePrice))
			return false;
		return true;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public Timestamp getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Timestamp dateAdded) {
		this.dateAdded = dateAdded;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
