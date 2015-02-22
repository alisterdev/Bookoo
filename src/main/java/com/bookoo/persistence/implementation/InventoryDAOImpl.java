package com.bookoo.persistence.implementation;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.bookoo.data.BookBean;
import com.bookoo.data.FileUploadBean;
import com.bookoo.data.reports.TotalSalesBean;
import com.bookoo.persistence.interfaces.InventoryDAO;

/**
 * Data Access routine implementation for the Inventory data bean
 * 
 * @author Alex Ilea, Darrel-Day Guerrero, Anna Rogozin
 * @version 1.0
 */
@Named("bookAction")
@RequestScoped
public class InventoryDAOImpl implements InventoryDAO, Serializable {
	
	private static final long serialVersionUID = 2454297162701352019L;
	
	private String type;
	private String search;
	private ArrayList<BookBean> booksList;
	
	@Inject
	private ReportsDAOImpl rdi;
	
	@Inject
	private BookBean book;
	@Inject 
	FileUploadBean file;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	// Default constructor
	public InventoryDAOImpl() {
		super();
		booksList = new ArrayList<BookBean>();
	}
	
	 public String getSize() {
         return String.valueOf(booksList.size());
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

	public String checkAddBook() throws SQLException {
		book.setCover(file.getNameFile());
		int test = addBookRecord();

		if (test == 1) {
			return new String("Success");
		} else
			return new String("fail");
	} 

	public String checkEditBook(BookBean bookT) throws SQLException {
		int test = editBookRecord(bookT);

		if (test == 1) {
			return new String("Success");
		} else
			return new String("fail");
	}
	
	public String checkEditAvailability(BookBean book) throws SQLException {
		int test = 0;
		if(book.isAvailable()){
			test = editBookAvailability(false, book.getId());
		}
		else{
			test = editBookAvailability(true, book.getId());
		}
		if (test == 1) {
			return new String("Success");
		} else
			return new String("fail");
	}

	/**
	 * Performs an insert of a book record. Accepts an BookBean that contains
	 * all fields to insert.
	 * 
	 * @param BookBean
	 *            book
	 * @throws SQLException
	 */
	@Override
	public int addBookRecord() throws SQLException {
		String preparedQuery = "INSERT INTO BOOK_INVENTORY (ISBN13, ISBN10, TITLE, AUTHOR, PUBLISHER, "
				+ "PAGES, GENRE, COVER, BOOK_TYPE, BOOK_FORMAT, DOWNLOAD_LINK, COPIES, WHOLESALE_PRICE, LIST_PRICE, SALE_PRICE, "
				+ "WEIGHT, DIMENSIONS, DATE_ADDED, AVAILABLE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setString(1, book.getIsbn13());
			ps.setString(2, book.getIsbn10());
			ps.setString(3, book.getTitle());
			ps.setString(4, book.getAuthor());
			ps.setString(5, book.getPublisher());
			ps.setInt(6, book.getPages());
			ps.setString(7, book.getGenre());
			ps.setString(8, book.getCover());
			ps.setInt(9, book.getBookType());
			ps.setInt(10, book.getBookFormat());
			ps.setString(11, book.getDownloadLink());
			ps.setInt(12, book.getCopies());
			ps.setBigDecimal(13, book.getWholesalePrice());
			ps.setBigDecimal(14, book.getListPrice());
			ps.setBigDecimal(15, book.getSalePrice());
			ps.setDouble(16, book.getWeight());
			ps.setString(17, book.getDimensions());
			
			ps.setTimestamp(18, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setBoolean(19, book.isAvailable());

			return ps.executeUpdate();
		}
	}
	
	@Override
	public int editBookAvailability(boolean availability, long id) throws SQLException {
		String preparedQuery = "UPDATE BOOK_INVENTORY SET AVAILABLE = ? WHERE BOOK_ID = ?";
		
		if (ds == null)
			throw new SQLException("Can't get datasource.");
		
		try(Connection connection = ds.getConnection();
				PreparedStatement ps = connection.prepareStatement(preparedQuery);){
			ps.setBoolean(1, availability);
			ps.setLong(2, id);
			
			return ps.executeUpdate();
		}
		
	}
	
	@Override
	public int editBookRecord(BookBean bookT) throws SQLException {
		System.out.println(bookT);
		String preparedQuery = "UPDATE BOOK_INVENTORY SET "
				+ "ISBN13 = ?, ISBN10 = ?, TITLE = ?, AUTHOR = ?, PUBLISHER = ?, "
				+ "PAGES = ?, GENRE = ?, COVER = ?, BOOK_TYPE = ?, BOOK_FORMAT = ?, DOWNLOAD_LINK =? , COPIES = ?, "
				+ "WHOLESALE_PRICE = ?, LIST_PRICE = ?, SALE_PRICE = ?, "
				+ "WEIGHT = ?, DIMENSIONS = ?, DATE_ADDED = ?, AVAILABLE = ? WHERE BOOK_ID = ?";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();

		PreparedStatement ps = connection.prepareStatement(preparedQuery);) {

			ps.setString(1, bookT.getIsbn13());
			ps.setString(2, bookT.getIsbn10());
			ps.setString(3, bookT.getTitle());
			ps.setString(4, bookT.getAuthor());
			ps.setString(5, bookT.getPublisher());
			ps.setInt(6, bookT.getPages());
			ps.setString(7, bookT.getGenre());
			ps.setString(8, bookT.getCover());
			ps.setInt(9, bookT.getBookType());
			ps.setInt(10, bookT.getBookFormat());
			ps.setString(11, bookT.getDownloadLink());
			ps.setInt(12, bookT.getCopies());
			ps.setBigDecimal(13, bookT.getWholesalePrice());
			ps.setBigDecimal(14, bookT.getListPrice());
			ps.setBigDecimal(15, bookT.getSalePrice());
			ps.setDouble(16, bookT.getWeight());
			ps.setString(17, bookT.getDimensions());
			ps.setTimestamp(18, bookT.getDateAdded());
			ps.setBoolean(19, bookT.isAvailable());
			ps.setLong(20, bookT.getId());

			return ps.executeUpdate();
		}
	}

	@Override
	public int removeBookRecord(BookBean book) throws SQLException {
		String preparedQuery = "UPDATE BOOK_INVENTORY SET AVAILABLE = FALSE WHERE BOOK_ID = ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();

		PreparedStatement ps = connection.prepareStatement(preparedQuery);) {

			ps.setLong(1, book.getId());
			return ps.executeUpdate();
		}
	}

	@Override
	public ArrayList<BookBean> getAllBooks() throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();

		if (ds == null)
			throw new SQLException("Can't get data source");

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY";

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);
				ResultSet rs = pStatement.executeQuery()) {
			while (rs.next()) {
				BookBean book = new BookBean();
				book.setId(rs.getLong("BOOK_ID"));
				book.setIsbn13(rs.getString("ISBN13"));
				book.setIsbn10(rs.getString("ISBN10"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				book.setPages(rs.getInt("PAGES"));
				book.setGenre(rs.getString("GENRE"));
				book.setCover(rs.getString("COVER"));
				book.setBookType(rs.getInt("BOOK_TYPE"));
				book.setBookFormat(rs.getInt("BOOK_FORMAT"));
				book.setDownloadLink(rs.getString("DOWNLOAD_LINK"));
				book.setCopies(rs.getInt("COPIES"));
				book.setWholesalePrice(rs.getBigDecimal("WHOLESALE_PRICE"));
				book.setListPrice(rs.getBigDecimal("LIST_PRICE"));
				book.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
				book.setWeight(rs.getDouble("WEIGHT"));
				book.setDimensions(rs.getString("DIMENSIONS"));
				book.setDateAdded(rs.getTimestamp("DATE_ADDED"));
				book.setAvailable(rs.getBoolean("AVAILABLE"));

				// add to books list
				booksList.add(book);
			}
		}
		return booksList;
	}

	public ArrayList<BookBean> getBooksOnSale() throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();

		if (ds == null)
			throw new SQLException("Can't get data source");

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE SALE_PRICE != 0";

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);
				ResultSet rs = pStatement.executeQuery()) {
			while (rs.next()) {
				BookBean book = new BookBean();
				book.setId(rs.getLong("BOOK_ID"));
				book.setIsbn13(rs.getString("ISBN13"));
				book.setIsbn10(rs.getString("ISBN10"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				book.setPages(rs.getInt("PAGES"));
				book.setGenre(rs.getString("GENRE"));
				book.setCover(rs.getString("COVER"));
				book.setBookType(rs.getInt("BOOK_TYPE"));
				book.setBookFormat(rs.getInt("BOOK_FORMAT"));
				book.setDownloadLink(rs.getString("DOWNLOAD_LINK"));
				book.setCopies(rs.getInt("COPIES"));
				book.setWholesalePrice(rs.getBigDecimal("WHOLESALE_PRICE"));
				book.setListPrice(rs.getBigDecimal("LIST_PRICE"));
				book.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
				book.setWeight(rs.getDouble("WEIGHT"));
				book.setDimensions(rs.getString("DIMENSIONS"));
				book.setDateAdded(rs.getTimestamp("DATE_ADDED"));
				book.setAvailable(rs.getBoolean("AVAILABLE"));

				// add to books list
				booksList.add(book);
			}
		}
		return booksList;
	}

	public ArrayList<BookBean> getBooksByRecentDate() throws SQLException {
		System.out.println("hi from book recent date");
		
		ArrayList<BookBean> booksList = new ArrayList<BookBean>();
		if (ds == null)
			throw new SQLException("Can't get data source");

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY ORDER BY DATE_ADDED DESC";

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);
				ResultSet rs = pStatement.executeQuery()) {
			while (rs.next()) {
				BookBean book = new BookBean();
				book.setId(rs.getLong("BOOK_ID"));
				book.setIsbn13(rs.getString("ISBN13"));
				book.setIsbn10(rs.getString("ISBN10"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				book.setPages(rs.getInt("PAGES"));
				book.setGenre(rs.getString("GENRE"));
				book.setCover(rs.getString("COVER"));
				book.setBookType(rs.getInt("BOOK_TYPE"));
				book.setBookFormat(rs.getInt("BOOK_FORMAT"));
				book.setDownloadLink(rs.getString("DOWNLOAD_LINK"));
				book.setCopies(rs.getInt("COPIES"));
				book.setWholesalePrice(rs.getBigDecimal("WHOLESALE_PRICE"));
				book.setListPrice(rs.getBigDecimal("LIST_PRICE"));
				book.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
				book.setWeight(rs.getDouble("WEIGHT"));
				book.setDimensions(rs.getString("DIMENSIONS"));
				book.setDateAdded(rs.getTimestamp("DATE_ADDED"));
				book.setAvailable(rs.getBoolean("AVAILABLE"));

				// add to books list
				booksList.add(book);
			}
		}
		return booksList;
	}

	public ArrayList<BookBean> getBooksMostSold() throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();

		if (ds == null)
			throw new SQLException("Can't get data source");

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY ORDER BY COPIES_SOLD DESC";

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);
				ResultSet rs = pStatement.executeQuery()) {
			while (rs.next()) {
				BookBean book = new BookBean();
				book.setId(rs.getLong("BOOK_ID"));
				book.setIsbn13(rs.getString("ISBN13"));
				book.setIsbn10(rs.getString("ISBN10"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				book.setPages(rs.getInt("PAGES"));
				book.setGenre(rs.getString("GENRE"));
				book.setCover(rs.getString("COVER"));
				book.setBookType(rs.getInt("BOOK_TYPE"));
				book.setBookFormat(rs.getInt("BOOK_FORMAT"));
				book.setDownloadLink(rs.getString("DOWNLOAD_LINK"));
				book.setCopies(rs.getInt("COPIES"));
				book.setWholesalePrice(rs.getBigDecimal("WHOLESALE_PRICE"));
				book.setListPrice(rs.getBigDecimal("LIST_PRICE"));
				book.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
				book.setWeight(rs.getDouble("WEIGHT"));
				book.setDimensions(rs.getString("DIMENSIONS"));
				book.setDateAdded(rs.getTimestamp("DATE_ADDED"));
				book.setAvailable(rs.getBoolean("AVAILABLE"));

				// add to books list
				booksList.add(book);
			}
		}
		return booksList;
	}

	@Override
	public ArrayList<BookBean> searchInventory(BookBean book)
			throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE ISBN10 = ? OR ISBN13 = ? OR TITLE LIKE ? OR AUTHOR LIKE ? OR PUBLISHER LIKE ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {
			ps.setString(1, book.getIsbn10());
			ps.setString(2, book.getIsbn13());
			ps.setString(3, book.getTitle());
			ps.setString(4, book.getAuthor());
			ps.setString(5, book.getPublisher());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}

		return booksList;
	}

	@Override
	public ArrayList<BookBean> searchInventoryByTitle(String title)
			throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();

		if (title.length() > 2)
			title = "%" + title + "%";

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE TITLE LIKE ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			ps.setString(1, title);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}

		return booksList;
	}

	public ArrayList<BookBean> searchInventoryByAuthor(String author)
			throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();
		
		if (author.length() > 2)
			author = "%" + author + "%";
		
		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE AUTHOR LIKE ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			ps.setString(1, author);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}

		return booksList;
	}

	@Override
	public ArrayList<BookBean> searchInventoryByCover(int cover)
			throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE BOOK_TYPE = ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			ps.setInt(1, cover);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}

		return booksList;
	}

	@Override
	public ArrayList<BookBean> searchInventoryByPublisher(String publisher)
			throws SQLException {
		
		ArrayList<BookBean> booksList = new ArrayList<BookBean>();

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE PUBLISHER LIKE ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			ps.setString(1, publisher);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}
		System.out.println(booksList);
		return booksList;
	}

	/**
	 * the type section allow us to define what operator will be used for the
	 * search possibilites: 1 : = 2 : > 3 : >= 4 : < 5 : <=
	 * 
	 * the price is the price
	 * 
	 */
	@Override
	public ArrayList<BookBean> searchInventoryByPrice(int type, BigDecimal price)
			throws SQLException {

		ArrayList<BookBean> booksList = new ArrayList<BookBean>();
		String preparedQuery = new String();
		switch (type) {
		case 1:
			preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE WHOLESALE_PRICE = ?";
			break;
		case 2:
			preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE WHOLESALE_PRICE > ?";
			break;
		case 3:
			preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE WHOLESALE_PRICE >= ?";
			break;
		case 4:
			preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE WHOLESALE_PRICE < ?";
			break;
		case 5:
			preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE WHOLESALE_PRICE <= ?";
			break;
		}

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			ps.setBigDecimal(1, price);

			System.out.println(ps);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}

		return booksList;
	}

	@Override
	public BookBean getBookById(long id) throws SQLException {

		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE BOOK_ID LIKE ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setLong(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					BookBean book = new BookBean();
					book.setAuthor(rs.getString("AUTHOR"));
					book.setAvailable(rs.getBoolean("AVAILABLE"));
					book.setBookType(rs.getInt("BOOK_TYPE"));
					book.setCopies(rs.getInt("COPIES"));
					book.setCover(rs.getString("COVER"));
					book.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					book.setDimensions(rs.getString("DIMENSIONS"));
					book.setBookFormat(rs.getInt("BOOK_FORMAT"));
					book.setGenre(rs.getString("GENRE"));
					book.setId(rs.getLong("BOOK_ID"));
					book.setIsbn10(rs.getString("ISBN10"));
					book.setIsbn13(rs.getString("ISBN13"));
					book.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					book.setPages(rs.getInt("PAGES"));
					book.setPublisher(rs.getString("PUBLISHER"));
					book.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					book.setTitle(rs.getString("TITLE"));
					book.setWeight(rs.getDouble("WEIGHT"));
					book.setWholesalePrice(rs.getBigDecimal("WHOLESALE_PRICE"));
					return book;
				}
			}
		}
		return null;
	}

	/**
	 * allow us to search for a book with a genre possibilies: Classic
	 * YoungAdult thriller children SciFiction
	 */
	@Override
	public ArrayList<BookBean> searchBookByGenre(String genre)
			throws SQLException {
		ArrayList<BookBean> booksList = new ArrayList<BookBean>();
		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE GENRE = ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {
			ps.setString(1, genre);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}

		return booksList;
	}

	@Override
	public ArrayList<BookBean> searchInventoryByISBN(String isbn)
			throws SQLException {
		
		ArrayList<BookBean> booksList = new ArrayList<BookBean>();
		String preparedQuery = "SELECT * FROM BOOK_INVENTORY WHERE ISBN10 LIKE ?";
		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			ps.setString(1, isbn);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookBean resultBook = new BookBean();
					resultBook.setAuthor(rs.getString("AUTHOR"));
					resultBook.setAvailable(rs.getBoolean("AVAILABLE"));
					resultBook.setBookType(rs.getInt("BOOK_TYPE"));
					resultBook.setCopies(rs.getInt("COPIES"));
					resultBook.setCover(rs.getString("COVER"));
					resultBook.setDateAdded(rs.getTimestamp("DATE_ADDED"));
					resultBook.setDimensions(rs.getString("DIMENSIONS"));
					resultBook.setBookFormat(rs.getInt("BOOK_FORMAT"));
					resultBook.setGenre(rs.getString("GENRE"));
					resultBook.setId(rs.getLong("BOOK_ID"));
					resultBook.setIsbn10(rs.getString("ISBN10"));
					resultBook.setIsbn13(rs.getString("ISBN13"));
					resultBook.setListPrice(rs.getBigDecimal("LIST_PRICE"));
					resultBook.setPages(rs.getInt("PAGES"));
					resultBook.setPublisher(rs.getString("PUBLISHER"));
					resultBook.setSalePrice(rs.getBigDecimal("SALE_PRICE"));
					resultBook.setTitle(rs.getString("TITLE"));
					resultBook.setWeight(rs.getDouble("WEIGHT"));
					resultBook.setWholesalePrice(rs
							.getBigDecimal("WHOLESALE_PRICE"));

					// add to books list
					booksList.add(resultBook);
				}
			}
		}

		return booksList;
	}

	/**
	 * Updates quantity of a book after a customer purchase.
	 * Temporary: books with a negative value of copies should 
	 * be no longer available for sale and set as out of stock.
	 * @param bookId
	 * @param quantitySold
	 * @return
	 * @throws SQLException
	 */
	public int updateBookQuantity(long bookId, int quantitySold)
			throws SQLException {
		String preparedQuery = "UPDATE book_inventory SET COPIES = COPIES - ?, COPIES_SOLD = COPIES_SOLD + ? WHERE BOOK_ID = ?";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			ps.setInt(1, quantitySold);
			ps.setInt(2, quantitySold);
			ps.setLong(3, bookId);

			return ps.executeUpdate();
		}
	}
	
	/**
	 * Allow us to get a list of books, which will depends of some parameters,
	 * actually store in the url: search = then entry of the user type = by:
	 * title, publisher, author, isbn, genre.
	 * 
	 * Because of the fact that we redirect the page, we have to add the post contruct annotation. 
	 * 
	 * @throws SQLException
	 */
	@PostConstruct
	public void consBooksList() throws SQLException {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		search = request.getParameter("search");
		type = request.getParameter("by");

		if (search != null && type != null) {
			if (booksList.size() != 0 || booksList != null)
				booksList.clear();

			if (type.trim().equalsIgnoreCase("title")) {
				booksList = searchInventoryByTitle(search);
			} else if (type.trim().equalsIgnoreCase("isbn")) {
				booksList = searchInventoryByISBN(search);
			} else if (type.trim().equalsIgnoreCase("author")) {
				booksList = searchInventoryByAuthor(search);
			} else if (type.trim().equalsIgnoreCase("publisher")) {
				booksList = searchInventoryByPublisher(search);
			} else if (type.trim().equalsIgnoreCase("genre")) {
				booksList = searchBookByGenre(search);
				setSearch("");
			} else if (type.trim().equalsIgnoreCase("cover")) {
				booksList = searchInventoryByCover(Integer.valueOf(search));
			} else if (type.trim().equalsIgnoreCase("price")) {
				if (Integer.valueOf(search) == 31)
					booksList = searchInventoryByPrice(2,
							BigDecimal.valueOf(Long.valueOf(search)));
				else
					booksList = searchInventoryByPrice(4,
							BigDecimal.valueOf(Long.valueOf(search)));
			}
		}
		
		// Redirects to a single book page if only one book found
		if (booksList.size() == 1 && !request.getPathInfo().toString().equals("/manager/inventorySearch.xhtml")) {
			long bookId = booksList.get(0).getId();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			try {
				response.sendRedirect(request.getContextPath() + "/faces/book.xhtml?bookId=" + bookId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
	}
	
	public ArrayList<BookBean> getBooksList() throws SQLException {
		return booksList;
	}
	
	/**
	 * With the method above: retrieve a list of publisher that the user has
	 * tried to search from this list: get all the report sales by all those
	 * publishers, and put them all in an ArrayList of TotalSalesBean
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
	 * same as above, but with author
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public ArrayList<TotalSalesBean> getAuthorSalesWithClientsList()
			throws SQLException, ParseException {

		ArrayList<TotalSalesBean> list = new ArrayList<>();

		com.bookoo.business.Report report = new com.bookoo.business.Report();
		getBooksList();
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
		booksList = searchBookByGenre("classic");
		return "o";
	}

	public String loadYoungAdult() throws SQLException {
		booksList = searchBookByGenre("YoungAdult");
		return "search";
	}

	public String loadThriller() throws SQLException {
		booksList = searchBookByGenre("thriller");
		return "search";
	}

	public String loadChildren() throws SQLException {
		booksList = searchBookByGenre("children");
		return "search";
	}

	public String loadScienceFiction() throws SQLException {
		booksList = searchBookByGenre("SciFiction");
		return "search";
	}

	public String loadHardCover() throws SQLException {
		booksList = searchInventoryByCover(0);
		booksList.addAll(searchInventoryByCover(1));
		// load also books which are available in hard cover and ebook format
		return "search";
	}

	public String loadSoftCover() throws SQLException {
		booksList = searchInventoryByCover(2);
		booksList.addAll(searchInventoryByCover(3));
		// load also books which are available in soft cover and ebook format
		return "search";
	}

	public String loadEbook() throws SQLException {
		booksList = searchInventoryByCover(4);
		booksList.addAll(searchInventoryByCover(1));
		// load also books which are available in hard cover and ebook format
		booksList.addAll(searchInventoryByCover(3));
		// load also books which are available in soft cover and ebook format
		return "search";
	}

	public String loadPriceUnder10() throws SQLException {
		booksList = searchInventoryByPrice(4,
				BigDecimal.valueOf(Long.valueOf(10)));
		return "search";
	}

	public String loadPriceUnder20() throws SQLException {
		booksList = searchInventoryByPrice(4,
				BigDecimal.valueOf(Long.valueOf(20)));
		return "search";
	}

	public String loadPriceUnder30() throws SQLException {
		booksList = searchInventoryByPrice(4,
				BigDecimal.valueOf(Long.valueOf(30)));
		return "search";
	}

	public String loadPriceAbove30() throws SQLException {
		booksList = searchInventoryByPrice(2,
				BigDecimal.valueOf(Long.valueOf(30)));
		return "search";
	}
}
