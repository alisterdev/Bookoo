package com.bookoo.persistence.implementation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import com.bookoo.data.BookBean;
import com.bookoo.data.ClientBean;
import com.bookoo.data.reports.TopClientBean;
import com.bookoo.data.reports.TopSellerBean;
import com.bookoo.data.reports.TotalSalesBean;
import com.bookoo.data.reports.ZeroSalesBean;
import com.bookoo.persistence.interfaces.ReportsDAO;

/**
 * Data Access Object for business reports
 * 
 * @author Anna Rogozin, Darrel-Day Guerrero, Alex Ilea
 * 
 */

@Named("reportsAction")
@RequestScoped
public class ReportsDAOImpl implements ReportsDAO, Serializable {

	private static final long serialVersionUID = -3997645965932376133L;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	/**
	 * Constructor
	 */
	public ReportsDAOImpl() {
		super();
	}

	/**
	 * Provides a summarized report of total sales in the database for a given
	 * time frame.
	 * 
	 * @param start
	 *            Start date
	 * @param end
	 *            End date
	 * @return Total Sales, Total Cost and Profit
	 */
	@Override
	public BigDecimal[][] reportTotalSalesSummary(Calendar start, Calendar end)
			throws SQLException {

		// 2D array for Total Sales , Total Cost and Profit
		BigDecimal[][] salesSummary = new BigDecimal[1][3];

		String preparedQuery = "SELECT (SELECT SUM(invoice.NET_PRICE) FROM invoice WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND invoice.AVAILABLE = 1) AS 'Total Sales', "
				+ "SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Total Cost', "
				+ "(SELECT SUM(invoice.NET_PRICE) FROM invoice WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND invoice.AVAILABLE = 1) - SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Profit' "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN book_inventory USING (BOOK_ID) "
				+ "WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND invoice.AVAILABLE = 1";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));
			pStatement.setTimestamp(3, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(4, new Timestamp(start.getTimeInMillis()));
			pStatement.setTimestamp(5, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(6, new Timestamp(start.getTimeInMillis()));

			try (ResultSet resultSet = pStatement.executeQuery()) {
				if (resultSet.next()) {
					salesSummary[0][0] = resultSet.getBigDecimal(1);
					salesSummary[0][1] = resultSet.getBigDecimal(2);
					salesSummary[0][2] = resultSet.getBigDecimal(3);
				}
			}
		}

		return salesSummary;
	}

	/**
	 * Provides a list of orders that make up the total sales in the database
	 * for a given time frame.
	 * 
	 * @param start
	 *            Start date
	 * @param end
	 *            End date
	 * @return List of total sales
	 */
	@Override
	public ArrayList<TotalSalesBean> reportTotalSales(Calendar start,
			Calendar end) throws SQLException {

		ArrayList<TotalSalesBean> salesList = new ArrayList<TotalSalesBean>();

		String preparedQuery = "SELECT invoice_details.BOOK_ID AS ID, book_inventory.TITLE AS Title, "
				+ "SUM(QUANTITY) AS Quantity, SUM(QUANTITY * BOOK_PRICE) AS Total, "
				+ "SALE_DATE AS Date "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN book_inventory USING (BOOK_ID) "
				+ "WHERE SALE_DATE <= ? AND SALE_DATE >= ? AND invoice.AVAILABLE = 1 AND invoice_details.AVAILABLE = 1 "
				+ "GROUP BY book_inventory.BOOK_ID, SALE_DATE "
				+ "ORDER BY SALE_DATE ASC";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					TotalSalesBean totalSale = new TotalSalesBean();
					totalSale.setBookID(resultSet.getLong("ID"));
					totalSale.setQuantity(resultSet.getInt("Quantity"));
					totalSale.setTitle(resultSet.getString("Title"));
					totalSale.setTotal(resultSet.getBigDecimal("Total"));

					Calendar temp = new GregorianCalendar(2008, 01, 01);
					temp.setTime(resultSet.getDate("Date"));

					totalSale.setDate(temp);
					salesList.add(totalSale);
				}
			}
		}

		return salesList;
	}

	/**
	 * Provides a summarized report of total sales of a client in the database
	 * for a given time frame.
	 * 
	 * @param start
	 *            Start date
	 * @param end
	 *            End date
	 * @return Total Sales, Total Cost and Profit
	 */
	@Override
	public BigDecimal[][] reportSalesByClientSummary(Calendar start,
			Calendar end, long clientId) throws SQLException {
		BigDecimal[][] salesSummary = new BigDecimal[1][3];
		String preparedQuery = "SELECT (SELECT SUM(invoice.NET_PRICE) FROM invoice WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND invoice.CLIENT_ID = ? AND invoice.AVAILABLE = 1) AS 'Total Sales', "
				+ "SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Total Cost', "
				+ "(SELECT SUM(invoice.NET_PRICE) FROM invoice WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND invoice.CLIENT_ID = ? AND invoice.AVAILABLE = 1) - SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Profit' "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN book_inventory USING (BOOK_ID) "
				+ "WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND invoice.CLIENT_ID = ? AND invoice.AVAILABLE = 1";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));
			pStatement.setLong(3, clientId);
			pStatement.setTimestamp(4, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(5, new Timestamp(start.getTimeInMillis()));
			pStatement.setLong(6, clientId);
			pStatement.setTimestamp(7, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(8, new Timestamp(start.getTimeInMillis()));
			pStatement.setLong(9, clientId);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				if (resultSet.next()) {
					salesSummary[0][0] = resultSet.getBigDecimal(1);
					salesSummary[0][1] = resultSet.getBigDecimal(2);
					salesSummary[0][2] = resultSet.getBigDecimal(3);
				}
			}
		}
		return salesSummary;
	}

	/**
	 * Provides a list of orders that make up the total sales for a client in
	 * the database for a given time frame.
	 * 
	 * @param start
	 *            Start date
	 * @param end
	 *            End date
	 * @return List of total sales
	 */
	@Override
	public ArrayList<TotalSalesBean> reportSalesByClient(Calendar start,
			Calendar end, long clientId) throws SQLException {

		ArrayList<TotalSalesBean> salesList = new ArrayList<TotalSalesBean>();

		String preparedQuery = "SELECT invoice_details.BOOK_ID AS ID, book_inventory.TITLE AS Title, "
				+ "QUANTITY AS Quantity, BOOK_PRICE AS Price, SUM(QUANTITY * BOOK_PRICE) AS Total, "
				+ "SALE_DATE as Date "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN book_inventory USING (BOOK_ID) "
				+ "WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND invoice.AVAILABLE = 1 "
				+ "AND invoice.CLIENT_ID = ? GROUP BY BOOK_ID "
				+ "ORDER BY SALE_DATE ASC";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));
			pStatement.setLong(3, clientId);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					TotalSalesBean totalSale = new TotalSalesBean();
					totalSale.setBookID(resultSet.getLong("ID"));
					totalSale.setPrice(resultSet.getBigDecimal("Price"));
					totalSale.setQuantity(resultSet.getInt("Quantity"));
					totalSale.setTitle(resultSet.getString("Title"));
					totalSale.setTotal(resultSet.getBigDecimal("Total"));

					Calendar temp = new GregorianCalendar(2008, 01, 01);
					temp.setTime(resultSet.getDate("Date"));

					totalSale.setDate(temp);
					salesList.add(totalSale);
				}
			}
		}

		return salesList;
	}

	/**
	 * Provides a summarized report of total sales of an author in the database
	 * for a given time frame.
	 * 
	 * @param start
	 *            Start date
	 * @param end
	 *            End date
	 * @return Total Sales, Total Cost and Profit
	 */
	@Override
	public BigDecimal[][] reportSalesByAuthorSummary(Calendar start,
			Calendar end, String authorName) throws SQLException {

		BigDecimal[][] salesSummary = new BigDecimal[1][3];

		String preparedQuery = "SELECT SUM(invoice.NET_PRICE) AS 'Total Sales', "
				+ "SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Total Cost', "
				+ "SUM(invoice.NET_PRICE) - SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Profit' "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN book_inventory USING (BOOK_ID) "
				+ "WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND book_inventory.AUTHOR = ? AND invoice.AVAILABLE = 1";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));
			pStatement.setString(3, authorName);
			try (ResultSet resultSet = pStatement.executeQuery()) {
				if (resultSet.next()) {
					salesSummary[0][0] = resultSet.getBigDecimal(1);
					salesSummary[0][1] = resultSet.getBigDecimal(2);
					salesSummary[0][2] = resultSet.getBigDecimal(3);
				}
			}
		}
		return salesSummary;
	}

	/**
	 * Provides a list of orders that make up the total sales for an author in
	 * the database for a given time frame.
	 * 
	 * @param start
	 *            Start date
	 * @param end
	 *            End date
	 * @return List of total sales
	 */
	@Override
	public ArrayList<TotalSalesBean> reportSalesByAuthor(Calendar start,
			Calendar end, String authorName) throws SQLException {
		ArrayList<TotalSalesBean> salesList = new ArrayList<TotalSalesBean>();
		String preparedQuery = "SELECT INVOICE_DETAILS.BOOK_ID AS ID, BOOK_INVENTORY.TITLE AS Title, "
				+ "QUANTITY AS Quantity, BOOK_PRICE AS Price, SUM(QUANTITY * BOOK_PRICE) AS Total, "
				+ "SALE_DATE as Date "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN BOOK_INVENTORY USING (BOOK_ID) "
				+ "WHERE SALE_DATE <= ? AND SALE_DATE >= ? AND book_inventory.AUTHOR = ? AND invoice.AVAILABLE = 1 "
				+ "GROUP BY BOOK_ID " + "ORDER BY SALE_DATE ASC";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));
			pStatement.setString(3, authorName);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					TotalSalesBean totalSale = new TotalSalesBean();
					totalSale.setBookID(resultSet.getLong("ID"));
					totalSale.setPrice(resultSet.getBigDecimal("Price"));
					totalSale.setQuantity(resultSet.getInt("Quantity"));
					totalSale.setTitle(resultSet.getString("Title"));
					totalSale.setTotal(resultSet.getBigDecimal("Total"));

					Calendar temp = new GregorianCalendar(2008, 01, 01);
					temp.setTime(resultSet.getDate("Date"));

					totalSale.setDate(temp);
					salesList.add(totalSale);
				}
			}
		}

		return salesList;
	}

	@Override
	public BigDecimal[][] reportSalesByPublisherSummary(Calendar start,
			Calendar end, String publisherName) throws SQLException {
		BigDecimal[][] salesSummary = new BigDecimal[1][3];
		String preparedQuery = "SELECT SUM(invoice.NET_PRICE) AS 'Total Sales', "
				+ "SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Total Cost', "
				+ "SUM(invoice.NET_PRICE) - SUM(book_inventory.WHOLESALE_PRICE * invoice_details.QUANTITY) AS 'Profit' "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN book_inventory USING (BOOK_ID) "
				+ "WHERE invoice.SALE_DATE <= ? AND invoice.SALE_DATE >= ? AND book_inventory.PUBLISHER = ? AND invoice.AVAILABLE = 1"
				+ "GROUP BY book_inventory.BOOK_ID ";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));
			pStatement.setString(3, publisherName);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				if (resultSet.next()) {
					salesSummary[0][0] = resultSet.getBigDecimal(1);
					salesSummary[0][1] = resultSet.getBigDecimal(2);
					salesSummary[0][2] = resultSet.getBigDecimal(3);
				}
			}
		}

		return salesSummary;
	}

	@Override
	public ArrayList<TotalSalesBean> reportSalesByPublisher(Calendar start,
			Calendar end, String publisherName) throws SQLException {
		ArrayList<TotalSalesBean> salesList = new ArrayList<TotalSalesBean>();
		String preparedQuery = "SELECT INVOICE_DETAILS.BOOK_ID AS ID, BOOK_INVENTORY.TITLE AS Title, "
				+ "QUANTITY AS Quantity, BOOK_PRICE AS Price, SUM(QUANTITY * BOOK_PRICE) AS Total, "
				+ "SALE_DATE as Date "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN BOOK_INVENTORY USING (BOOK_ID) "
				+ "WHERE SALE_DATE <= ? AND SALE_DATE >= ? AND book_inventory.PUBLISHER = ? AND invoice.AVAILABLE = 1 "
				+ "GROUP BY BOOK_ID " + "ORDER BY SALE_DATE ASC";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));
			pStatement.setString(3, publisherName);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					TotalSalesBean totalSale = new TotalSalesBean();
					totalSale.setBookID(resultSet.getLong("ID"));
					totalSale.setPrice(resultSet.getBigDecimal("Price"));
					totalSale.setQuantity(resultSet.getInt("Quantity"));
					totalSale.setTitle(resultSet.getString("Title"));
					totalSale.setTotal(resultSet.getBigDecimal("Total"));

					Calendar temp = new GregorianCalendar(2008, 01, 01);
					temp.setTime(resultSet.getDate("Date"));

					totalSale.setDate(temp);
					salesList.add(totalSale);
				}
			}
		}

		return salesList;
	}

	@Override
	public ArrayList<TopSellerBean> reportTopSellers(Calendar start,
			Calendar end) throws SQLException {
		ArrayList<TopSellerBean> topSellersList = new ArrayList<TopSellerBean>();
		String preparedQuery = "SELECT book_inventory.book_id AS 'Book ID', "
				+ "book_inventory.title AS 'Title', SUM(invoice_details.quantity) AS 'Quantity Sold',"
				+ "SALE_DATE as Date "
				+ "FROM invoice JOIN invoice_details USING(SALE_ID) JOIN book_inventory USING (BOOK_ID) "
				+ "WHERE SALE_DATE <= ? AND SALE_DATE >= ? AND invoice.AVAILABLE = 1 "
				+ "GROUP BY invoice_details.book_id HAVING SUM(invoice_details.quantity) > 0 "
				+ "ORDER BY SUM(invoice_details.quantity) DESC";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					TopSellerBean topSeller = new TopSellerBean();
					topSeller.setBookId(resultSet.getLong("Book ID"));
					topSeller
							.setQuantitySold(resultSet.getInt("Quantity Sold"));
					topSeller.setTitle(resultSet.getString("Title"));

					Calendar temp = new GregorianCalendar(2008, 01, 01);
					temp.setTime(resultSet.getDate("Date"));

					topSeller.setDate(temp);
					topSellersList.add(topSeller);
				}
			}
		}

		return topSellersList;
	}

	@Override
	public BigDecimal getClientValue(ClientBean client) throws SQLException {
		BigDecimal total = new BigDecimal(0);

		String preparedQuery = "SELECT SUM(invoice.net_price) AS 'Total' FROM invoice WHERE CLIENT_ID = ?;";

		if (ds == null)
			throw new SQLException("Can't retrieve data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {
			ps.setLong(1, client.getId());
			try (ResultSet resultSet = ps.executeQuery();) {
				while (resultSet.next()) {
					total = resultSet.getBigDecimal("Total");
				}
			}
		}
		return total;
	}

	@Override
	public List<TopClientBean> reportTopClients(Calendar start, Calendar end)
			throws SQLException {
		List<TopClientBean> topClientsList = new ArrayList<TopClientBean>();

		String preparedQuery = "SELECT clients.client_id AS 'Client ID', clients.lastname AS 'Last Name', "
				+ "clients.firstname AS 'First Name', SUM(invoice.net_price) AS 'Total', "
				+ "SALE_DATE as Date "
				+ "FROM clients JOIN invoice USING (client_id) "
				+ "WHERE SALE_DATE <= ? AND SALE_DATE >= ? AND invoice.AVAILABLE = 1 "
				+ "GROUP BY clients.client_id HAVING SUM(invoice.net_price) > 0 "
				+ "ORDER BY Total DESC";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					TopClientBean topClient = new TopClientBean();
					topClient.setClientID(resultSet.getLong("Client ID"));
					topClient.setFirstName(resultSet.getString("First Name"));
					topClient.setLastName(resultSet.getString("Last Name"));
					topClient.setTotal(resultSet.getBigDecimal("Total"));

					Calendar temp = new GregorianCalendar(2008, 01, 01);
					temp.setTime(resultSet.getDate("Date"));

					topClient.setDate(temp);
					topClientsList.add(topClient);
				}
			}
		}

		return topClientsList;
	}

	@Override
	public List<ZeroSalesBean> reportUnsoldBooks(Calendar start, Calendar end)
			throws SQLException {
		List<ZeroSalesBean> unsoldList = new ArrayList<ZeroSalesBean>();

		String preparedQuery = "SELECT DISTINCT book_inventory.book_id, book_inventory.isbn10, book_inventory.title, "
				+ "book_inventory.author, book_inventory.sale_price,  book_inventory.list_price, book_inventory.copies,"
				+ " book_inventory.available, invoice.SALE_DATE AS 'Date'"
				+ "FROM book_inventory, invoice "
				+ "WHERE book_id NOT IN (SELECT invoice_details.book_id "
				+ "FROM invoice_details "
				+ "LEFT JOIN invoice ON invoice_details.SALE_ID=invoice.SALE_ID "
				+ "WHERE SALE_DATE <= ? AND SALE_DATE >= ? AND invoice.AVAILABLE = 1 ) "
				+ "GROUP BY book_inventory.book_id";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, new Timestamp(end.getTimeInMillis()));
			pStatement.setTimestamp(2, new Timestamp(start.getTimeInMillis()));

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {

					ZeroSalesBean unsoldBook = new ZeroSalesBean();

					unsoldBook.setAuthor(resultSet.getString("AUTHOR"));
					unsoldBook.setAvailable(resultSet.getBoolean("AVAILABLE"));
					unsoldBook.setCopies(resultSet.getInt("COPIES"));
					unsoldBook.setId(resultSet.getLong("BOOK_ID"));
					unsoldBook.setIsbn10(resultSet.getString("ISBN10"));
					unsoldBook.setListPrice(resultSet
							.getBigDecimal("LIST_PRICE"));
					unsoldBook.setSalePrice(resultSet
							.getBigDecimal("SALE_PRICE"));
					unsoldBook.setTitle(resultSet.getString("TITLE"));

					Calendar temp = new GregorianCalendar(2008, 01, 01);

					temp.setTime(resultSet.getDate("Date"));

					unsoldBook.setDate(temp);

					unsoldList.add(unsoldBook);
				}
			}
		}

		return unsoldList;
	}

	@Override
	public List<BookBean> getInventoryReport() throws SQLException {
		List<BookBean> stockList = new ArrayList<BookBean>();

		String preparedQuery = "SELECT * FROM book_inventory WHERE COPIES > 0;";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			ResultSet resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				BookBean book = new BookBean();
				book.setAuthor(resultSet.getString("AUTHOR"));
				book.setAvailable(resultSet.getBoolean("AVAILABLE"));
				book.setBookType(resultSet.getInt("BOOK_TYPE"));
				book.setCopies(resultSet.getInt("COPIES"));
				book.setCover(resultSet.getString("COVER"));
				book.setDateAdded(resultSet.getTimestamp("DATE_ADDED"));
				book.setDimensions(resultSet.getString("DIMENSIONS"));
				book.setBookFormat(resultSet.getInt("BOOK_FORMAT"));
				book.setGenre(resultSet.getString("GENRE"));
				book.setId(resultSet.getLong("BOOK_ID"));
				book.setIsbn10(resultSet.getString("ISBN10"));
				book.setIsbn13(resultSet.getString("ISBN13"));
				book.setListPrice(resultSet.getBigDecimal("LIST_PRICE"));
				book.setPages(resultSet.getInt("PAGES"));
				book.setPublisher(resultSet.getString("PUBLISHER"));
				book.setSalePrice(resultSet.getBigDecimal("SALE_PRICE"));
				book.setTitle(resultSet.getString("TITLE"));
				book.setWeight(resultSet.getDouble("WEIGHT"));
				book.setWholesalePrice(resultSet
						.getBigDecimal("WHOLESALE_PRICE"));
				book.setDownloadLink(resultSet.getString("DOWNLOAD_LINK"));

				stockList.add(book);
			}
		}

		return stockList;
	}

	@Override
	public BigDecimal[][] getInventoryValue() throws SQLException {

		BigDecimal[][] inventoryValue = new BigDecimal[1][2];

		String preparedQuery = "SELECT (SELECT SUM(WHOLESALE_PRICE * COPIES))AS 'Wholesale Value',"
				+ " (SELECT SUM(LIST_PRICE * COPIES)) AS 'Listprice Value' "
				+ "FROM book_inventory;";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			try (ResultSet resultSet = ps.executeQuery();) {
				if (resultSet.next()) {
					inventoryValue[0][0] = resultSet.getBigDecimal(1);
					inventoryValue[0][1] = resultSet.getBigDecimal(2);
				}
			}
		}

		return inventoryValue;
	}

	@Override
	public List<BookBean> getLowInventoryReport() throws SQLException {
		List<BookBean> lowInventoryList = new ArrayList<BookBean>();

		String preparedQuery = "SELECT BOOK_ID, ISBN13, ISBN10, TITLE, AUTHOR, PUBLISHER, COPIES "
				+ "FROM book_inventory WHERE COPIES < 5";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {
			ResultSet resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				BookBean lowInventoryBook = new BookBean();
				lowInventoryBook.setAuthor(resultSet.getString("AUTHOR"));
				lowInventoryBook.setCopies(resultSet.getInt("COPIES"));
				lowInventoryBook.setId(resultSet.getLong("BOOK_ID"));
				lowInventoryBook.setIsbn10(resultSet.getString("ISBN10"));
				lowInventoryBook.setIsbn13(resultSet.getString("ISBN13"));
				lowInventoryBook.setPublisher(resultSet.getString("PUBLISHER"));
				lowInventoryBook.setTitle(resultSet.getString("TITLE"));

				lowInventoryList.add(lowInventoryBook);
			}
		}

		return lowInventoryList;
	}

}
