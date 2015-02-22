/**
 * 
 */
package com.bookoo.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.bookoo.data.BookBean;
import com.bookoo.data.CartItemBean;
import com.bookoo.persistence.implementation.InventoryDAOImpl;

/**
 * Shopping Cart Bean representing potential items to be purchased by the
 * client. Handles cart item insert, cart item quantity update and cart reset.
 * 
 * @author Anna Rogozin, Darrel-Day Guerrero
 * 
 */
@Named
@SessionScoped
public class ShoppingCart implements Serializable {

	// No provincial taxes for books, therefore 5% for all provinces
	private final BigDecimal TAXES = new BigDecimal("0.05");
	private static final long serialVersionUID = -2154848887048208343L;

	private ArrayList<CartItemBean> booksInCart;
	private InventoryDAOImpl inventory;
	private int currentQuantity;
	private String currentBookType;
	private int cartSize;
	private BigDecimal subTotal;
	private BigDecimal calculatedTaxes;
	private BigDecimal grandTotal;
	private boolean emptyCart;

	/**
	 * Constructor
	 */
	public ShoppingCart() {
		super();
		booksInCart = new ArrayList<CartItemBean>();
		inventory = new InventoryDAOImpl();
		subTotal = new BigDecimal(0);
		calculatedTaxes = new BigDecimal(0);
		cartSize = 0;
		currentQuantity = 1;
		emptyCart = true;
	}

	public ArrayList<CartItemBean> getBooksInCart() {
		return booksInCart;
	}

	public void setBooksInCart(ArrayList<CartItemBean> booksInCart) {
		this.booksInCart = booksInCart;
	}

	public void setEmptyCart(boolean emptyCart) {
		this.emptyCart = emptyCart;
	}

	public boolean getEmptyCart() {
		return emptyCart;
	}

	public void setSubTotal(BigDecimal amount) {
		this.subTotal = amount;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	/**
	 * @return the currentQuantity
	 */
	public int getCurrentQuantity() {
		return currentQuantity;
	}

	/**
	 * @param currentQuantity
	 *            the currentQuantity to set
	 */
	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public String getCurrentBookType() {
		return currentBookType;
	}

	public void setCurrentBookType(String currentType) {
		this.currentBookType = currentType;
	}

	public int getCartSize() {
		return cartSize;
	}

	public String addToCart() {
		// get the ID of the book that was clicked
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		long bookId = Long.parseLong(params.get("bookId"));

		CartItemBean bookInCart = new CartItemBean();

		try {
			BookBean book = inventory.getBookById(bookId);
			bookInCart.setBook(book);
			bookInCart.setQuantity(currentQuantity);

			if (currentBookType == null)
				currentBookType = getAppropriateType(book);
			bookInCart.setTypeSelected(currentBookType);

			// checks if selected book is already in the cart
			boolean alreadyInCart = false;
			int i = 0;
			int size = booksInCart.size();
			while (!alreadyInCart && i < size) {
				if (book.getId() == booksInCart.get(i).getBookId())
					alreadyInCart = true;
				else
					i++;
			}

			if (alreadyInCart)
				booksInCart.get(i).setQuantity(
						booksInCart.get(i).getQuantity() + currentQuantity);
			else {
				booksInCart.add(bookInCart);
				if (emptyCart)
					emptyCart = false;
			}
			cartSize += currentQuantity;
			currentQuantity = 1;
			currentBookType = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Calculates sub total based on user's shopping cart items.
	 * 
	 * @return sub total amount
	 */
	public BigDecimal getSubTotal() {
		BigDecimal amount = BigDecimal.ZERO;
		for (CartItemBean item : getBooksInCart()) {
			amount = amount.add(item.getTotal());
		}
		subTotal = amount;
		return subTotal;
	}

	/**
	 * Calculates total amount of taxes for user's order
	 * 
	 * @return calculated taxes
	 */
	public BigDecimal getCalculatedTaxes() {
		calculatedTaxes = getSubTotal().multiply(TAXES);
		return calculatedTaxes;
	}

	/**
	 * Calculates grand total for user's order, including taxes
	 * 
	 * @return grand
	 */
	public BigDecimal getGrandTotal() {
		grandTotal = getSubTotal().add(getCalculatedTaxes());
		return grandTotal;
	}

	/**
	 * 
	 * @param book
	 * @return
	 */
	private String getAppropriateType(BookBean book) {
		String type = "";
		ResourceBundle msgs = ResourceBundle
				.getBundle("com/bookoo/bundles/messages_book");

		switch (book.getBookType()) {
		case 0: // hard cover
			type = msgs.getString("hard_cover");
			break;
		case 2: // soft cover
			type = msgs.getString("soft_cover");
			break;
		case 1: // ebook
		case 3:
		case 4:
			type = msgs.getString("ebook");
			break;
		}
		return type;
	}

	/**
	 * Keep track of quantity of books in the shopping cart
	 * 
	 * @param itemIndex
	 */
	public void updateCart(int itemIndex) {
		if (booksInCart.get(itemIndex).getQuantity() < 1)
			booksInCart.remove(itemIndex);

		// update cart size
		cartSize = 0;
		for (CartItemBean item : booksInCart)
			cartSize += item.getQuantity();

		if (cartSize == 0)
			emptyCart = true;
	}

	/**
	 * Resets the shopping cart after purchase
	 * 
	 * @return "Shopping cart reset" navigation outcome
	 */
	public String resetCart() {

		// update books quantity in database before resetting
		try {
			for (CartItemBean item : booksInCart)

				inventory.updateBookQuantity(item.getBookId(),
						item.getQuantity());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		booksInCart = new ArrayList<CartItemBean>();
		inventory = new InventoryDAOImpl();
		subTotal = new BigDecimal(0);
		calculatedTaxes = new BigDecimal(0);
		cartSize = 0;
		currentQuantity = 1;
		emptyCart = true;

		return "Shopping cart reset";
	}

}
