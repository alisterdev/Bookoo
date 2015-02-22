package com.bookoo.persistence.interfaces;

import com.bookoo.data.BookBean;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access routine interface for the Inventory data bean
 * 
 * @author Alex Ilea, Darrel-Day Guerrero
 * @version 1.0
 */
public interface InventoryDAO {

	public int addBookRecord() throws SQLException;


	public int removeBookRecord(BookBean book) throws SQLException;

	public ArrayList<BookBean> searchInventory(BookBean book) throws SQLException;
	
	public ArrayList<BookBean> searchInventoryByTitle(String title) throws SQLException;
	public ArrayList<BookBean> searchInventoryByAuthor(String author) throws SQLException;
	public ArrayList<BookBean> searchInventoryByPublisher(String publisher) throws SQLException;
	public ArrayList<BookBean> searchInventoryByISBN(String isbn) throws SQLException;

	public ArrayList<BookBean> searchBookByGenre(String genre) throws SQLException;
	public ArrayList<BookBean> getAllBooks() throws SQLException; // optional
	public ArrayList<BookBean> getBooksOnSale() throws SQLException;
	public ArrayList<BookBean> getBooksByRecentDate() throws SQLException;
	public ArrayList<BookBean> getBooksMostSold() throws SQLException;
	public ArrayList<BookBean> searchInventoryByCover(int cover) throws SQLException;

	public BookBean getBookById(long id) throws SQLException;
	
	public ArrayList<BookBean> searchInventoryByPrice(int type, BigDecimal price) throws SQLException;

	public int editBookAvailability(boolean availability, long id) throws SQLException;

	public int editBookRecord(BookBean bookT) throws SQLException;
}
