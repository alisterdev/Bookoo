package com.bookoo.persistence.implementation;

import com.bookoo.business.ShoppingCart;
import com.bookoo.business.UserSession;
import com.bookoo.data.CartItemBean;
import com.bookoo.data.InvoiceBean;
import com.bookoo.persistence.interfaces.InvoiceDAO;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * Data Access Object for client's sales invoice
 * 
 * @author Jolan Cornevin, Darrel-Day Guerrero, Alex Ilea
 * @version 1.0
 */
@Named("invoiceAction")
@RequestScoped
public class InvoiceDAOImpl implements InvoiceDAO, Serializable {

	private static final long serialVersionUID = -2180606151297287000L;
	private String type;
	private String search;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	@Inject
	private InvoiceDetailsDAOImpl invoiceDetails;

	@Inject
	private UserSession user;

	@Inject
	private ShoppingCart cart;

	public InvoiceDAOImpl() {
		super();
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

	/**
	 * Forward purchase by inserting invoice and set navigation case
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String forwardPurchase() throws SQLException {

		long result = this.insertInvoice();
		ArrayList<CartItemBean> invoiceItems = cart.getBooksInCart();
		invoiceDetails.fetchSalesId();

		for (CartItemBean item : invoiceItems) {
			System.out.println(item.toString());
			invoiceDetails.addInvoiceItem(item);
		}

		if (result == 1)
			return "Purchase succeeded";
		else
			return "Purchase failed";
	}

	/*
	 * Insert the invoice inside the INVOICE table
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.InvoiceDAO#insertInvoice(com.bookoo
	 * .data.InvoiceBean)
	 */
	@Override
	public int insertInvoice() throws SQLException {
		String preparedQuery = "INSERT INTO INVOICE (SALE_DATE, CLIENT_ID, NET_PRICE, PST, GST, HST, GROSS_PRICE, AVAILABLE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {
			GregorianCalendar gc = new GregorianCalendar();
			ps.setTimestamp(1, new Timestamp(gc.getTimeInMillis()));
			ps.setLong(2, user.getClient().getId());
			ps.setBigDecimal(3, cart.getSubTotal());
			ps.setBigDecimal(4, user.getPST());
			ps.setBigDecimal(5, user.getGST());
			ps.setBigDecimal(6, user.getHST());
			ps.setBigDecimal(7, cart.getGrandTotal());
			ps.setBoolean(8, true);

			return ps.executeUpdate();
		}
	}

	/*
	 * Update all values of the invoice
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.InvoiceDAO#updateInvoice(com.bookoo
	 * .data.InvoiceBean)
	 */
	@Override
	public int updateInvoice(InvoiceBean invoice) throws SQLException {
		String preparedQuery = "UPDATE INVOICE SET SALE_DATE = ?, CLIENT_ID = ?, NET_PRICE = ?, PST = ?, GST = ?, HST = ?, GROSS_PRICE = ?, AVAILABLE = ? WHERE SALE_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setTimestamp(1, invoice.getSaleDate());
			ps.setLong(2, invoice.getClientId());
			ps.setBigDecimal(3, invoice.getNetPrice());
			ps.setBigDecimal(4, invoice.getPST());
			ps.setBigDecimal(5, invoice.getGST());
			ps.setBigDecimal(6, invoice.getHST());
			ps.setBigDecimal(7, invoice.getGrossPrice());
			ps.setBoolean(8, invoice.isAvailable());
			ps.setLong(9, invoice.getSaleId());

			return ps.executeUpdate();
		}
	}

	@Override
	public int updateDetails(InvoiceBean invoice) throws SQLException {
		String preparedQuery = "UPDATE INVOICE_DETAILS SET AVAILABLE = ? WHERE SALE_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setBoolean(1, invoice.isAvailable());
			ps.setLong(2, invoice.getSaleId());

			return ps.executeUpdate();
		}
	}
	
	/*
	 * set AVAILABLE = false for the invoice given in parameter
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.InvoiceDAO#removeInvoice(com.bookoo
	 * .data.InvoiceBean)
	 */
	@Override
	public int removeInvoice(InvoiceBean invoice) throws SQLException {
		String preparedQuery = "UPDATE INVOICE SET AVAILABLE = FALSE WHERE SALE_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setLong(1, invoice.getSaleId());

			return ps.executeUpdate();
		}
	}

	/*
	 * get all invoice
	 * 
	 * @see com.bookoo.persistence.interfaces.InvoiceDAO#getAllInvoices()
	 */
	@Override
	public ArrayList<InvoiceBean> getAllInvoices() throws SQLException {
		ArrayList<InvoiceBean> results = new ArrayList<>();

		String preparedQuery = "SELECT * FROM INVOICE";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					InvoiceBean invoice = new InvoiceBean();

					invoice.setSaleId(resultSet.getLong("SALE_ID"));
					invoice.setSaleDate(resultSet.getTimestamp("SALE_DATE"));
					invoice.setClientId(resultSet.getLong("CLIENT_ID"));
					invoice.setNetPrice(resultSet.getBigDecimal("NET_PRICE"));
					invoice.setPST(resultSet.getBigDecimal("PST"));
					invoice.setGST(resultSet.getBigDecimal("GST"));
					invoice.setHST(resultSet.getBigDecimal("HST"));
					invoice.setGrossPrice(resultSet
							.getBigDecimal("GROSS_PRICE"));
					invoice.setAvailable(resultSet.getBoolean("AVAILABLE"));

					results.add(invoice);
				}
			}
		}

		return results;
	}

	/*
	 * get all invoice where CIENT_ID = id (given in parameter)
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.InvoiceDAO#getAllInvoicesByClientId
	 * (long)
	 */
	@Override
	public ArrayList<InvoiceBean> getAllInvoicesByClientId(long id)
			throws SQLException {

		ArrayList<InvoiceBean> results = new ArrayList<>();

		String preparedQuery = "SELECT * FROM INVOICE WHERE CLIENT_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setLong(1, id);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					InvoiceBean invoice = new InvoiceBean();

					invoice.setSaleId(resultSet.getLong("SALE_ID"));
					invoice.setSaleDate(resultSet.getTimestamp("SALE_DATE"));
					invoice.setClientId(resultSet.getLong("CLIENT_ID"));
					invoice.setNetPrice(resultSet.getBigDecimal("NET_PRICE"));
					invoice.setPST(resultSet.getBigDecimal("PST"));
					invoice.setGST(resultSet.getBigDecimal("GST"));
					invoice.setHST(resultSet.getBigDecimal("HST"));
					invoice.setGrossPrice(resultSet
							.getBigDecimal("GROSS_PRICE"));
					invoice.setAvailable(resultSet.getBoolean("AVAILABLE"));

					results.add(invoice);
				}
			}
		}

		return results;
	}

	/*
	 * get all invoice where SALE_ID = id (given in parameter)
	 * 
	 * @see com.bookoo.persistence.interfaces.InvoiceDAO#getInvoiceById(long)
	 */
	@Override
	public InvoiceBean getInvoiceById(long id) throws SQLException {
		InvoiceBean invoice = new InvoiceBean();

		String preparedQuery = "SELECT * FROM INVOICE WHERE SALE_ID=?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {
			
			pStatement.setLong(1, id);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {

					invoice.setSaleId(resultSet.getLong("SALE_ID"));
					invoice.setSaleDate(resultSet.getTimestamp("SALE_DATE"));
					invoice.setClientId(resultSet.getLong("CLIENT_ID"));
					invoice.setNetPrice(resultSet.getBigDecimal("NET_PRICE"));
					invoice.setPST(resultSet.getBigDecimal("PST"));
					invoice.setGST(resultSet.getBigDecimal("GST"));
					invoice.setHST(resultSet.getBigDecimal("HST"));
					invoice.setGrossPrice(resultSet
							.getBigDecimal("GROSS_PRICE"));
					invoice.setAvailable(resultSet.getBoolean("AVAILABLE"));
				}
			}
		}

		return invoice;
	}

	/*
	 * get all invoice of the day.
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.InvoiceDAO#getAllInvoicesByDate(java
	 * .sql.Timestamp)
	 */
	@Override
	public ArrayList<InvoiceBean> getAllInvoicesByDate(Timestamp date)
			throws SQLException {

		ArrayList<InvoiceBean> results = new ArrayList<>();

		String preparedQuery = "SELECT * FROM INVOICE WHERE SALE_ID=?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			pStatement.setTimestamp(1, date);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					InvoiceBean invoice = new InvoiceBean();

					invoice.setSaleId(resultSet.getLong("SALE_ID"));
					invoice.setSaleDate(resultSet.getTimestamp("SALE_DATE"));
					invoice.setClientId(resultSet.getLong("CLIENT_ID"));
					invoice.setNetPrice(resultSet.getBigDecimal("NET_PRICE"));
					invoice.setPST(resultSet.getBigDecimal("PST"));
					invoice.setGST(resultSet.getBigDecimal("GST"));
					invoice.setHST(resultSet.getBigDecimal("HST"));
					invoice.setGrossPrice(resultSet
							.getBigDecimal("GROSS_PRICE"));
					invoice.setAvailable(resultSet.getBoolean("AVAILABLE"));

					results.add(invoice);
				}
			}
		}

		return results;
	}

	/**
	 * Searches invoices from database based on field and type.
	 * 
	 * @param field
	 *            search keyword
	 * @param type
	 *            Type of search
	 * @return Prepared SQL query
	 */
	@Override
	public ArrayList<InvoiceBean> searchInvoiceByField(String field, String type)
			throws SQLException {

		ArrayList<InvoiceBean> invoices = new ArrayList<InvoiceBean>();

		// Retrieves preparedQuery from the found in the last position of the
		// object list and casts into a String.
		String preparedQuery = createSQL(type);

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
			
			if (type.equals("id")) {
				Long id;
				try {
					id = Long.parseLong(field);
				} catch (NumberFormatException e) {
					id = 0L;
				}
				ps.setLong(1, id);
			}

			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					InvoiceBean invoice = new InvoiceBean();

					invoice.setSaleId(resultSet.getLong("SALE_ID"));
					invoice.setSaleDate(resultSet.getTimestamp("SALE_DATE"));
					invoice.setClientId(resultSet.getLong("CLIENT_ID"));
					invoice.setNetPrice(resultSet.getBigDecimal("NET_PRICE"));
					invoice.setPST(resultSet.getBigDecimal("PST"));
					invoice.setGST(resultSet.getBigDecimal("GST"));
					invoice.setHST(resultSet.getBigDecimal("HST"));
					invoice.setGrossPrice(resultSet
							.getBigDecimal("GROSS_PRICE"));
					invoice.setAvailable(resultSet.getBoolean("AVAILABLE"));

					// add to invoice list
					invoices.add(invoice);
				}
			}
		}
		return invoices;
	}

	/**
	 * Retrieves list of orders based on string title and type. Invoices can be
	 * retrieved based on order id and client id.
	 * 
	 * @return List of orders
	 */
	@Override
	public ArrayList<InvoiceBean> getInvoiceList() throws SQLException {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		ArrayList<InvoiceBean> invoiceList = new ArrayList<InvoiceBean>();

		search = request.getParameter("search");
		type = request.getParameter("by");

		if (search != null && type != null) {
			if (type.trim().equalsIgnoreCase("sale id")) {
				invoiceList = searchInvoiceByField(search, "saleid");
			} else if (type.trim().equalsIgnoreCase("client id")) {
				invoiceList = searchInvoiceByField(search, "clientid");
			} else {
				invoiceList = getAllInvoices();
			}
		}

		return invoiceList;
	}

	/**
	 * Defines a SQL prepared query based on the given type of search.
	 * 
	 * @param field
	 * @return Prepared SQL query
	 */
	private String createSQL(String field) {
		String preparedQuery = "SELECT * FROM invoice";
		String whereClause = " WHERE";
		String initialWhereClause = whereClause;

		switch (field) {
		case "saleid":
			whereClause += " SALE_ID = ?";
			break;
		case "clientid":
			whereClause += " CLIENT_ID = ?";
			break;
		}

		if (!whereClause.equals(initialWhereClause)) {
			preparedQuery += whereClause;
		}

		return preparedQuery;
	}

}