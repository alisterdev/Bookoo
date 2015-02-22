package com.bookoo.business;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookoo.data.BookBean;
import com.bookoo.data.reports.TotalSalesBean;
import com.bookoo.persistence.implementation.InventoryDAOImpl;
import com.bookoo.persistence.implementation.ReportsDAOImpl;

/**
 * This class provides the functionality to search for a book.
 * 
 * @author Jolan Cornevin, Darrel-Day Guerrero
 * 
 */
@Named
@RequestScoped
public class BookSearch implements Serializable {

	private static final long serialVersionUID = 8847598142730776081L;

	private String type;
	private String search;
	private ArrayList<BookBean> booksList;

	@Inject
	private InventoryDAOImpl idi;
	@Inject
	private ReportsDAOImpl rdi;

	public BookSearch() {
		super();
		booksList = new ArrayList<>();
	}

	public String getSize() {
		return String.valueOf(booksList.size());
	}

	/**
	 * Allows us to get a list of books, which depends on parameters stored in
	 * the url: search = then entry of the user; by(type) = title, publisher,
	 * author, isbn, genre.
	 * 
	 * @return An arraylist of books
	 * @throws SQLException
	 */
	public ArrayList<BookBean> getBooksList() throws SQLException {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		search = request.getParameter("search");
		type = request.getParameter("by");

		if (search != null && type != null) {
			if (booksList.size() != 0 || booksList != null)
				booksList.clear();

			if (type.trim().equalsIgnoreCase("title")) {
				booksList = idi.searchInventoryByTitle(search);
			} else if (type.trim().equalsIgnoreCase("isbn")) {
				booksList = idi.searchInventoryByISBN(search);
			} else if (type.trim().equalsIgnoreCase("author")) {
				booksList = idi.searchInventoryByAuthor(search);
			} else if (type.trim().equalsIgnoreCase("publisher")) {
				booksList = idi.searchInventoryByPublisher(search);
			} else if (type.trim().equalsIgnoreCase("genre")) {
				booksList = idi.searchBookByGenre(search);
				this.setSearch("");
			} else if (type.trim().equalsIgnoreCase("cover")) {
				booksList = idi.searchInventoryByCover(Integer.valueOf(search));
			} else if (type.trim().equalsIgnoreCase("price")) {
				if (Integer.valueOf(search) == 31)
					booksList = idi.searchInventoryByPrice(2,
							BigDecimal.valueOf(Long.valueOf(search)));
				else
					booksList = idi.searchInventoryByPrice(4,
							BigDecimal.valueOf(Long.valueOf(search)));
			}
		}

		if (booksList.size() == 1) {
			long bookId = booksList.get(0).getId();
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			try {
				response.sendRedirect(request.getContextPath()
						+ "/faces/book.xhtml?" + bookId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return booksList;
	}

	/**
	 * This method retrieves sales by the publisher requested by the user.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public ArrayList<TotalSalesBean> getPublisherSalesWithClientsList()
			throws SQLException, ParseException {

		ArrayList<TotalSalesBean> list = new ArrayList<>();

		com.bookoo.business.Report report = new com.bookoo.business.Report();
		getBooksList();

		for (BookBean cb : booksList) {
			list.addAll(rdi.reportSalesByPublisher(report.getCheckin(),
					report.getCheckout(), cb.getPublisher()));
		}
		return list;
	}

	/**
	 * This method retrieves sales by the author requested by the user.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public ArrayList<TotalSalesBean> getAuthorSalesWithClientsList()
			throws SQLException, ParseException {

		ArrayList<TotalSalesBean> list = new ArrayList<>();

		com.bookoo.business.Report report = new com.bookoo.business.Report();
		booksList = getBooksList();
		for (BookBean cb : booksList) {
			list.addAll(rdi.reportSalesByAuthor(report.getCheckin(),
					report.getCheckout(), cb.getAuthor()));
		}
		return list;
	}

	public void setBooksList(ArrayList<BookBean> booksList) {
		this.booksList = booksList;
	}

	public String loadClassic() throws SQLException {
		booksList = idi.searchBookByGenre("classic");
		return "o";
	}

	public String loadYoungAdult() throws SQLException {
		booksList = idi.searchBookByGenre("YoungAdult");
		return "search";
	}

	public String loadThriller() throws SQLException {
		booksList = idi.searchBookByGenre("thriller");
		return "search";
	}

	public String loadChildren() throws SQLException {
		booksList = idi.searchBookByGenre("children");
		return "search";
	}

	public String loadScienceFiction() throws SQLException {
		booksList = idi.searchBookByGenre("SciFiction");
		return "search";
	}

	/**
	 * Loads all books which are available in hard cover format
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String loadHardCover() throws SQLException {
		booksList = idi.searchInventoryByCover(0);
		booksList.addAll(idi.searchInventoryByCover(1));
		return "search";
	}

	/**
	 * Loads all books which are available in spft cover format
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String loadSoftCover() throws SQLException {
		booksList = idi.searchInventoryByCover(2);
		booksList.addAll(idi.searchInventoryByCover(3));
		return "search";
	}

	/**
	 * Loads all books which are available in ebook format
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String loadEbook() throws SQLException {
		booksList = idi.searchInventoryByCover(4);
		booksList.addAll(idi.searchInventoryByCover(1));
		booksList.addAll(idi.searchInventoryByCover(3));
		return "search";
	}

	public String loadPriceUnder10() throws SQLException {
		booksList = idi.searchInventoryByPrice(4,
				BigDecimal.valueOf(Long.valueOf(10)));
		return "search";
	}

	public String loadPriceUnder20() throws SQLException {
		booksList = idi.searchInventoryByPrice(4,
				BigDecimal.valueOf(Long.valueOf(20)));
		return "search";
	}

	public String loadPriceUnder30() throws SQLException {
		booksList = idi.searchInventoryByPrice(4,
				BigDecimal.valueOf(Long.valueOf(30)));
		return "search";
	}

	public String loadPriceAbove30() throws SQLException {
		booksList = idi.searchInventoryByPrice(2,
				BigDecimal.valueOf(Long.valueOf(30)));
		return "search";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((booksList == null) ? 0 : booksList.hashCode());
		result = prime * result + ((search == null) ? 0 : search.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		BookSearch other = (BookSearch) obj;
		if (booksList == null) {
			if (other.booksList != null)
				return false;
		} else if (!booksList.equals(other.booksList))
			return false;
		if (search == null) {
			if (other.search != null)
				return false;
		} else if (!search.equals(other.search))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}