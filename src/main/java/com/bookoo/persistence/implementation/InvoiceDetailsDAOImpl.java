package com.bookoo.persistence.implementation;

import com.bookoo.data.BookBean;
import com.bookoo.data.CartItemBean;
import com.bookoo.data.InvoiceBean;
import com.bookoo.data.InvoiceDetailsBean;
import com.bookoo.persistence.interfaces.InvoiceDetailsDAO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 * Data Access Object for customer's sales invoice
 * 
 * @author Jolan Cornevin, Darrel-Day Guerrero, Alex Ilea, Anna Rogozin
 * @version 1.0
 */

@Named("invoiceDetailsAction")
@RequestScoped
public class InvoiceDetailsDAOImpl implements InvoiceDetailsDAO, Serializable {

	private static final long serialVersionUID = -6450893602492950879L;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	private long saleID; // Sale ID for the invoice

	public InvoiceDetailsDAOImpl() {
		super();
	}

	public long getSaleID() {
		return saleID;
	}

	public void setSaleID(long saleID) {
		this.saleID = saleID;
	}
	
	/**
	 * Edit availability of an invoice book
	 * @param book
	 * @return
	 * @throws SQLException
	 */
	public String checkEditAvailability(InvoiceDetailsBean book) throws SQLException {
		int test = 0;
		if(book.isAvailable()){
			test = editBookAvailability(false, book.getBookId());
		}
		else{
			test = editBookAvailability(true, book.getBookId());
		}
		if (test == 1) {
			return new String("Success");
		} else
			return new String("fail");
	}

	/**
	 * Toggle a book's availability
	 * @param b boolean value
	 * @param bookId book invoice value
	 * @return SQL result
	 * @throws SQLException
	 */
	public int editBookAvailability(boolean b, long bookId) throws SQLException {
		String preparedQuery = "UPDATE INVOICE_DETAILS SET AVAILABLE = ? WHERE BOOK_ID = ?";
		
		if (ds == null)
			throw new SQLException("Can't get datasource.");
		
		try(Connection connection = ds.getConnection();
				PreparedStatement ps = connection.prepareStatement(preparedQuery);){
			ps.setBoolean(1, b);
			ps.setLong(2, bookId);
			
			return ps.executeUpdate();
		}
		
	}

	/**
	 * Add details for each item purchased from the order
	 * 
	 * @param CartItemBean
	 *            item (select sale_id from invoice where sale_id =
	 *            last_insert_id())
	 * @return inserted row count
	 */
	@Override
	public int addInvoiceItem(CartItemBean item) throws SQLException {
		String preparedQuery = "INSERT INTO INVOICE_DETAILS (SALE_ID, QUANTITY, BOOK_ID, BOOK_PRICE, AVAILABLE) values (?, ?, ?, ?, ?)";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {
			ps.setLong(1, saleID);
			ps.setInt(2, item.getQuantity());
			ps.setLong(3, item.getBookId());
			ps.setBigDecimal(4, item.getBook().getActualPrice());
			ps.setBoolean(5, true);

			return ps.executeUpdate();
		}
	}

	/**
	 * Update book copies after purchase
	 * @return SQL result
	 * @throws SQLException
	 */
	@Override
	public int updateBookCopies(CartItemBean item) throws SQLException {
		String preparedQuery = "UPDATE BOOK_INVENTORY SET COPIES = COPIES - ? WHERE BOOK_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {
			ps.setInt(1, item.getQuantity());
			ps.setLong(2, item.getBookId());
			return ps.executeUpdate();
		}
	}
	
	/**
	 * Retrieves all invoice details from the database 
	 * @return List of all the invoice
	 * @throws SQLException
	 */
	public ArrayList<InvoiceDetailsBean> getAll() throws SQLException {

		ArrayList<InvoiceDetailsBean> results = new ArrayList<InvoiceDetailsBean>();
		String preparedQuery = "SELECT * FROM INVOICE_DETAILS";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);
				ResultSet resultSet = ps.executeQuery()) {
			while (resultSet.next()) {
				InvoiceDetailsBean invoiced = new InvoiceDetailsBean();
				invoiced.setDetailsId(resultSet.getLong("DETAILS_ID"));
				invoiced.setSaleId(resultSet.getLong("SALE_ID"));
				invoiced.setQuantity(resultSet.getInt("QUANTITY"));
				invoiced.setBookId(resultSet.getLong("BOOK_ID"));
				invoiced.setBookPrice(BigDecimal.valueOf(resultSet
						.getInt("BOOK_PRICE")));
				invoiced.setAvailable(resultSet.getBoolean("AVAILABLE"));

				results.add(invoiced);

			}

		}

		return results;
	}

	/*
	 * Update all information of the invoice in parameter except it's id
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.InvoiceDetailsDAO#updateInvoiceItem
	 * (com.bookoo.data.InvoiceDetailsBean)
	 */
	@Override
	public int updateInvoiceItem(InvoiceDetailsBean invoice)
			throws SQLException {
		String preparedQuery = "UPDATE INVOICE_DETAILS SET SALE_ID = ?, QUANTITY = ?, BOOK_ID = ?, BOOK_PRICE = ?, AVAILABLE = ? WHERE DETAILS_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setLong(1, invoice.getSaleId());
			ps.setInt(2, invoice.getQuantity());
			ps.setLong(3, invoice.getBookId());
			ps.setBigDecimal(4, invoice.getBookPrice());
			ps.setBoolean(5, invoice.isAvailable());
			ps.setLong(6, invoice.getDetailsId());
			return ps.executeUpdate();
		}
	}

	/*
	 * set to false the available field
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.InvoiceDetailsDAO#removeInvoiceItem
	 * (com.bookoo.data.InvoiceDetailsBean)
	 */
	@Override
	public int removeInvoiceItem(InvoiceDetailsBean invoice)
			throws SQLException {
		String preparedQuery = "UPDATE INVOICE_DETAILS SET AVAILABLE = FALSE WHERE DETAILS_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {
			ps.setLong(1, invoice.getDetailsId());

			return ps.executeUpdate();
		}
	}

	/**
	 * Get ordered items and details of a existing invoice.
	 */
	@Override
	public ArrayList<InvoiceDetailsBean> getInvoiceDetailsBySaleId(
			InvoiceBean invoice) throws SQLException {

		String preparedQuery = "SELECT DETAILS_ID, SALE_ID, invoice_details.BOOK_ID, QUANTITY, BOOK_PRICE, invoice_details.AVAILABLE, "
				+ "book_inventory.TITLE, book_inventory.AUTHOR, book_inventory.DOWNLOAD_LINK "
				+ "FROM INVOICE_DETAILS JOIN BOOK_INVENTORY USING(BOOK_ID) WHERE SALE_ID = ?";
		ArrayList<InvoiceDetailsBean> invoiceDetailsList = new ArrayList<InvoiceDetailsBean>();

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {
			ps.setLong(1, invoice.getSaleId());

			try (ResultSet resultSet = ps.executeQuery()) {

				while (resultSet.next()) {
					InvoiceDetailsBean details = new InvoiceDetailsBean();
					details.setDetailsId(resultSet.getLong("DETAILS_ID"));
					details.setSaleId(resultSet.getLong("SALE_ID"));
					details.setQuantity(resultSet.getInt("QUANTITY"));
					details.setBookId(resultSet.getLong("BOOK_ID"));
					details.setBookPrice(resultSet.getBigDecimal("BOOK_PRICE"));
					details.setTitle(resultSet.getString("TITLE"));
					details.setAuthor(resultSet.getString("AUTHOR"));
					details.setDownloadLink(resultSet
							.getString("DOWNLOAD_LINK"));
					details.setAvailable(resultSet.getBoolean("AVAILABLE"));

					invoiceDetailsList.add(details);
				}
			}
		}
		return invoiceDetailsList;
	}

	/**
	 * Retrieves ID of the current invoice being processed.
	 * 
	 * @throws SQLException
	 */
	public void fetchSalesId() throws SQLException {

		String preparedQuery = "SELECT LAST_INSERT_ID()";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					setSaleID(resultSet.getLong("last_insert_id()"));
				}
			}
		}
	}
}
