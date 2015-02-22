package com.bookoo.business;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookoo.data.BookBean;
import com.bookoo.persistence.implementation.InventoryDAOImpl;

/**
 * This class provides the functionality for displaying a book on the book page.
 * 
 * @author Anna Rogozin, Alex Ilea.
 * 
 */
@Named
@RequestScoped
public class CurrentBook implements Serializable {

	private static final long serialVersionUID = -8895669801676247469L;

	private BookBean book;
	private Collection<SelectItem> quantityItems;
	private Collection<SelectItem> bookTypeItems;

	/**
	 * Display the appropriate book according to the book ID provided as a
	 * request parameter.
	 * 
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@PostConstruct
	public void initialize() throws NumberFormatException, SQLException {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		
		String bookId = request.getParameter("bookId");

		if (bookId == null || bookId.equals(""))
			redirectPage();

		try {

			InventoryDAOImpl temp = new InventoryDAOImpl();
			book = temp.getBookById(Long.valueOf(bookId));

			if (book != null) {
				setQuantityItems();
				setBookTypeItems();
			} else {
				redirectPage();
			}
		} catch (NumberFormatException e) {
			redirectPage();
		}

	}

	public void setBook(BookBean book) {
		this.book = book;
	}

	public BookBean getBook() {
		return book;
	}

	public Collection<SelectItem> getQuantityItems() {
		return quantityItems;
	}

	/**
	 * This method sets the quantity of the book that the user can add to the
	 * cart, which defaults to 5. If less books are available in the database,
	 * the quantity is set to the number of books available.
	 */
	private void setQuantityItems() {
		quantityItems = new ArrayList<SelectItem>();
		quantityItems.add(new SelectItem(1));

		// set maximum quantity to be selected
		int max = 5;
		if (book.getCopies() < max)
			max = book.getCopies();

		for (int i = 2; i <= max; ++i)
			quantityItems.add(new SelectItem(i));
	}

	public Collection<SelectItem> getBookTypeItems() {
		return bookTypeItems;
	}

	/**
	 * This method sets the formats available for the book displayed.
	 */
	private void setBookTypeItems() {
		bookTypeItems = new ArrayList<SelectItem>();
		ResourceBundle msgs = ResourceBundle
				.getBundle("com/bookoo/bundles/messages_book");

		switch (book.getBookType()) {
		case 0: // hard cover
			bookTypeItems.add(new SelectItem(msgs.getString("hard_cover")));
			break;
		case 1: // hard cover + ebook
			bookTypeItems.add(new SelectItem(msgs.getString("hard_cover")));
			bookTypeItems.add(new SelectItem(msgs.getString("ebook")));
			break;
		case 2: // soft cover
			bookTypeItems.add(new SelectItem(msgs.getString("soft_cover")));
			break;
		case 3: // soft cover + ebook
			bookTypeItems.add(new SelectItem(msgs.getString("soft_cover")));
			bookTypeItems.add(new SelectItem(msgs.getString("ebook")));
			break;
		case 4: // ebook
			bookTypeItems.add(new SelectItem(msgs.getString("ebook")));
			break;
		}
	}

	public String getBookId() throws SQLException {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String bookId = request.getParameter("bookId");

		if (bookId == null || bookId.equals(""))
			redirectPage();

		try {
			InventoryDAOImpl temp = new InventoryDAOImpl();
			book = temp.getBookById(Long.valueOf(bookId));

			if (book == null)
				redirectPage();
			else
				setQuantityItems();

		} catch (NumberFormatException e) {
			redirectPage();
		}
		return null;
	}

	private void redirectPage() {
		
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			try {
				response.sendRedirect("home.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("WHATS GUCCI?");
				e.printStackTrace();
			}
	}
}
