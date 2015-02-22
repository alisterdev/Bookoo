package com.bookoo.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 * Data bean representing Book
 * 
 * @author Darrel-Day Guerrero, Alex Ilea
 * 
 */
@Named("bookBean")
@RequestScoped
public class BookBean implements Serializable {

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

	public BookBean() {
		super();
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
	 * Custom method needed to return a String representation of a book type.
	 * 
	 * @return String bookType
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

	/**
	 * Validate Title AJAX
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateTitle(FacesContext fc, UIComponent c, Object value) {
		if ((((String) value).length()) < 1 || (((String) value).length()) > 15)
			throw new ValidatorException(new FacesMessage(
					"Please use between 1 and 150 characters."));
	}

	/**
	 * Validate Author AJAX
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateAuthor(FacesContext fc, UIComponent c, Object value) {
		if (!((String) value)
				.matches("^([a-zA-Z]{1,}[\\-\\s]{0,1}[A-Za-z]{0,}){3,35}$")) {
			throw new ValidatorException(new FacesMessage(
					"Please use between 3 and 50 characters, letters only."));
		}
	}

	/**
	 * Validate Publisher AJAX
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validatePublisher(FacesContext fc, UIComponent c, Object value) {
		if (!((String) value).matches("^[a-zA-Z0-9.? ]*$")) {
			throw new ValidatorException(new FacesMessage(
					"Please use alphanumeric characters only."));
		}
	}

	/**
	 * Validate Pages AJAX
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validatePages(FacesContext fc, UIComponent c, Object value) {

		String spages = ((String) value).trim();
		try {
			int pages = Integer.parseInt(spages);
			if (pages < 0)
				throw new ValidatorException(new FacesMessage(
						"Please enter a valid number of copies."));
		} catch (NumberFormatException e) {
			throw new ValidatorException(new FacesMessage(
					"Please enter a valid number of copies."));
		}
	}

	/**
	 * Validate Pages AJAX
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateCopies(FacesContext fc, UIComponent c, Object value) {
		String scopies = ((String) value).trim();

		try {
			int copies = Integer.parseInt(scopies);
			if (copies < 0)
				throw new ValidatorException(new FacesMessage(
						"Please enter a valid number of copies."));
		} catch (NumberFormatException e) {
			throw new ValidatorException(new FacesMessage(
					"Please enter a valid number of copies."));
		}
	}

	/**
	 * Validate Download Link AJAX
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateDownloadLink(FacesContext fc, UIComponent c,
			Object value) {
		String url = ((String) value).trim();

		if (url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
			throw new ValidatorException(new FacesMessage(
					"Please enter a valid URL"));
		}
	}

	/**
	 * Validate ISBN10 AJAX
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateISBN10(FacesContext fc, UIComponent c, Object value) {
		char[] isbn = ((String) value).toCharArray();

		if (isbn.length != 10) {
			throw new ValidatorException(new FacesMessage(
					"ISBN must contain 10 alphanumeric characters."));
		} else {
			if (!checkISBN10(isbn)) {
				throw new ValidatorException(new FacesMessage(
						"Invalid ISBN-10 code, please verify."));
			}
		}
	}

	/**
	 * Validate ISBN13 AJAX
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateISBN13(FacesContext fc, UIComponent c, Object value) {
		char[] isbn = ((String) value).toCharArray();

		if (isbn.length != 13) {
			throw new ValidatorException(new FacesMessage(
					"ISBN must contain 13 alphanumeric characters."));
		} else {
			if (!checkISBN13(isbn)) {
				throw new ValidatorException(new FacesMessage(
						"Invalid ISBN-13 code, please verify."));
			}
		}
	}

	/**
	 * Check if set of characters is a valid ISBN 10 number
	 * 
	 * @param digits
	 * @return
	 */
	private boolean checkISBN10(char[] digits) {
		int a = 0, b = 0;

		for (int i = 0; i < 10; i++) {
			a += digits[i];
			b += a;
		}

		return b % 11 == 0;
	}

	/**
	 * Check if set of characters is a valid ISBN 13 number
	 * 
	 * @param digits
	 * @return
	 */
	private boolean checkISBN13(char[] digits) {
		int a = 0, check1, check2;

		for (int i = 0; i < 12; i++) {
			if (i % 2 == 0) {
				a += Character.getNumericValue(digits[i]);
			} else {
				a += Character.getNumericValue(digits[i]) * 3;
			}

		}
		check1 = Character.getNumericValue(digits[12]);
		check2 = a % 10;
		
		return (check1 == check2);
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
		BookBean other = (BookBean) obj;
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

	@Override
	public String toString() {
		return "BookBean [id=" + id + ", isbn13=" + isbn13 + ", isbn10="
				+ isbn10 + ", title=" + title + ", author=" + author
				+ ", publisher=" + publisher + ", pages=" + pages + ", genre="
				+ genre + ", cover=" + cover + ", bookType=" + bookType
				+ ", bookFormat=" + bookFormat + ", downloadLink="
				+ downloadLink + ", copies=" + copies + ", wholesalePrice="
				+ wholesalePrice + ", listPrice=" + listPrice + ", salePrice="
				+ salePrice + ", actualPrice=" + actualPrice + ", weight="
				+ weight + ", dimensions=" + dimensions + ", dateAdded="
				+ dateAdded + ", isAvailable=" + isAvailable + "]";
	}
}