package com.bookoo.persistence.implementation;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.bookoo.business.Report;
import com.bookoo.data.ClientBean;
import com.bookoo.data.reports.TotalSalesBean;
import com.bookoo.persistence.interfaces.ClientsDAO;
import com.bookoo.util.HashPassword;

/**
 * Data Access routine implementation for the Client Data Bean
 * 
 * @author Darrel-Day Guerrero
 * @version 1.0
 */

@Named("clientAction")
@RequestScoped
public class ClientDAOImpl implements ClientsDAO, Serializable {

	private static final long serialVersionUID = 4138672974186336336L;
	private String type;
	private String search;
	private ArrayList<ClientBean> clientList;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	@Inject
	private ClientBean client;

	@Inject
	private ReportsDAOImpl rdi;

	@Inject
	private Report report;

	public ClientDAOImpl() {
		super();
		clientList = new ArrayList<ClientBean>();
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

	public void setClientList(ArrayList<ClientBean> clientList) {
		this.clientList = clientList;
	}

	/**
	 * Proceed user insert and provides the appropriate String navigation case
	 * result.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String checkRegistration() throws SQLException {

		long result = this.insertClientRecord();

		if (result == 1)
			return "Registration succeeded";
		else
			return "Registration failed";
	}

	/**
	 * Proceed user update and provides the appropriate String navigation case
	 * result.
	 * 
	 * @param formClient
	 * @return Navigation case string result
	 * @throws SQLException
	 */
	public String checkUpdate(ClientBean formClient) throws SQLException {

		// For some reason, update only works when passed as a parameter.
		// Application loses state of the Injected Client before commandButton
		// action is executed.
		long result = this.updateUserProfile(formClient);
		long result2 = this.updateUserPassword(formClient);

		if (result == 1 && result2 == 1)
			return "Update succeeded";
		else
			return "Update failed";
	}

	public int setManager(ClientBean clientT) throws SQLException {
		if (clientT.isManager()) {
			String preparedQuery = "UPDATE CLIENTS SET IS_MANAGER = false "
					+ "WHERE CLIENT_ID = ?";

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
				ps.setLong(1, clientT.getId());
				return ps.executeUpdate();
			}
		} else {
			String preparedQuery = "UPDATE CLIENTS SET IS_MANAGER = true "
					+ "WHERE CLIENT_ID = ?";

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
				ps.setLong(1, clientT.getId());
				return ps.executeUpdate();
			}
		}
	}

	/**
	 * Proceed user deletion and provides the appropriate String navigation case
	 * result.
	 * 
	 * @param formClient
	 * @return Navigation case string result
	 * @throws SQLException
	 */
	public String checkDelete(ClientBean formClient) throws SQLException {
		long result = this.updateUserProfile(formClient);

		if (result == 1)
			return "Deletion succeeded";
		else
			return "Deletion failed";
	}

	/**
	 * Inserts a client record
	 * 
	 * @param client
	 *            The Client data bean
	 */
	@Override
	public int insertClientRecord() throws SQLException {

		String preparedQuery = "INSERT INTO clients (USERNAME, PASSWORD, IS_MANAGER, TITLE, LASTNAME, FIRSTNAME, COMPANY_NAME, "
				+ "ADDRESS1, ADDRESS2, CITY, PROVINCE, COUNTRY, POSTAL_CODE, SHIP_TITLE, SHIP_LAST_NAME, SHIP_FIRST_NAME, "
				+ "SHIP_COMPANY_NAME, SHIP_ADDRESS1, SHIP_ADDRESS2, SHIP_CITY, SHIP_PROVINCE, SHIP_COUNTRY, SHIP_POSTALCODE, "
				+ "PHONE_NUMBER, CELL_NUMBER, EMAIL, LAST_GENRE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

			ps.setString(1, client.getUsername());
			ps.setString(2, HashPassword.getSaltedHash(client.getPassword()));
			ps.setBoolean(3, client.isManager());
			ps.setString(4, client.getTitle());
			ps.setString(5, client.getLastName());
			ps.setString(6, client.getFirstName());
			ps.setString(7, client.getCompanyName());
			ps.setString(8, client.getAddress1());
			ps.setString(9, client.getAddress2());
			ps.setString(10, client.getCity());
			ps.setString(11, client.getProvince());
			ps.setString(12, client.getCountry());
			ps.setString(13, client.getPostalCode());
			ps.setString(14, client.getShipTitle());
			ps.setString(15, client.getShipLastName());
			ps.setString(16, client.getShipFirstName());
			ps.setString(17, client.getShipCompanyName());
			ps.setString(18, client.getShipAddress1());
			ps.setString(19, client.getShipAddress2());
			ps.setString(20, client.getShipCity());
			ps.setString(21, client.getShipProvince());
			ps.setString(22, client.getShipCountry());
			ps.setString(23, client.getShipPostalCode());
			ps.setString(24, client.getPhoneNumber());
			ps.setString(25, client.getCellNumber());
			ps.setString(26, client.getEmail());
			ps.setString(27, client.getLastGenre());

			return ps.executeUpdate();

		}
	}

	/**
	 * Update information of an existing client form the database.
	 * 
	 * @param formClient
	 *            ClientBean
	 * @return result code
	 * @throws SQLException
	 */
	@Override
	public int updateUserProfile(ClientBean formClient) throws SQLException {

		String preparedQuery = "UPDATE CLIENTS SET TITLE = ?,LASTNAME = ?, "
				+ "FIRSTNAME = ?, COMPANY_NAME = ?, ADDRESS1 = ?, ADDRESS2 = ?, CITY = ?, PROVINCE = ?,"
				+ "COUNTRY = ?, POSTAL_CODE = ?, PHONE_NUMBER = ?, CELL_NUMBER = ?, EMAIL = ?, SHIP_TITLE = ?, SHIP_LAST_NAME = ?, SHIP_FIRST_NAME = ?, "
				+ "SHIP_COMPANY_NAME = ?, SHIP_ADDRESS1 = ?, SHIP_ADDRESS2 = ?, SHIP_CITY = ?, SHIP_PROVINCE = ?, SHIP_COUNTRY = ?, SHIP_POSTALCODE = ? "
				+ "WHERE CLIENT_ID = ?";

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

			ps.setString(1, formClient.getTitle());
			ps.setString(2, formClient.getLastName());
			ps.setString(3, formClient.getFirstName());
			ps.setString(4, formClient.getCompanyName());
			ps.setString(5, formClient.getAddress1());
			ps.setString(6, formClient.getAddress2());
			ps.setString(7, formClient.getCity());
			ps.setString(8, formClient.getProvince());
			ps.setString(9, formClient.getCountry());
			ps.setString(10, formClient.getPostalCode());
			ps.setString(11, formClient.getPhoneNumber());
			ps.setString(12, formClient.getCellNumber());
			ps.setString(13, formClient.getEmail());
			ps.setString(14, formClient.getShipTitle());
			ps.setString(15, formClient.getShipLastName());
			ps.setString(16, formClient.getShipFirstName());
			ps.setString(17, formClient.getShipCompanyName());
			ps.setString(18, formClient.getShipAddress1());
			ps.setString(19, formClient.getShipAddress2());
			ps.setString(20, formClient.getShipCity());
			ps.setString(21, formClient.getShipProvince());
			ps.setString(22, formClient.getShipCountry());
			ps.setString(23, formClient.getShipPostalCode());
			ps.setLong(24, formClient.getId());

			return ps.executeUpdate();
		}

	}

	/**
	 * Update password information of an existing client form the database.
	 * 
	 * @param formClient
	 *            ClientBean
	 * @return result code
	 * @throws SQLException
	 */

	public int updateUserPassword(ClientBean formClient) throws SQLException {

		String preparedQuery = "UPDATE CLIENTS SET PASSWORD = ? WHERE CLIENT_ID = ?";

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

			ps.setString(1,
					HashPassword.getSaltedHash(formClient.getPassword()));
			ps.setLong(2, formClient.getId());

			return ps.executeUpdate();
		}

	}

	/**
	 * Retrieve all clients
	 * 
	 */
	@Override
	public ArrayList<ClientBean> getAllClients() throws SQLException {

		ArrayList<ClientBean> results = new ArrayList<>();
		String selectSQL = "SELECT * FROM CLIENTS";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(selectSQL);
				ResultSet resultSet = pStatement.executeQuery()) {
			while (resultSet.next()) {
				// Process results
				ClientBean client = new ClientBean();
				client.setId(resultSet.getLong("CLIENT_ID"));
				client.setUsername(resultSet.getString("USERNAME"));
				client.setPassword(resultSet.getString("PASSWORD"));
				client.setManager(resultSet.getBoolean("IS_MANAGER"));
				client.setTitle(resultSet.getString("TITLE"));
				client.setLastName(resultSet.getString("LASTNAME"));
				client.setFirstName(resultSet.getString("FIRSTNAME"));
				client.setCompanyName(resultSet.getString("COMPANY_NAME"));
				client.setAddress1(resultSet.getString("ADDRESS1"));
				client.setAddress2(resultSet.getString("ADDRESS2"));
				client.setCity(resultSet.getString("CITY"));
				client.setProvince(resultSet.getString("PROVINCE"));
				client.setCountry(resultSet.getString("COUNTRY"));
				client.setPostalCode(resultSet.getString("POSTAL_CODE"));
				client.setShipTitle(resultSet.getString("SHIP_TITLE"));
				client.setShipLastName(resultSet.getString("SHIP_LAST_NAME"));
				client.setShipFirstName(resultSet.getString("SHIP_FIRST_NAME"));
				client.setShipCompanyName(resultSet
						.getString("SHIP_COMPANY_NAME"));
				client.setShipAddress1(resultSet.getString("SHIP_ADDRESS1"));
				client.setShipAddress1(resultSet.getString("SHIP_ADDRESS2"));
				client.setShipCity(resultSet.getString("SHIP_CITY"));
				client.setProvince(resultSet.getString("SHIP_PROVINCE"));
				client.setShipCountry(resultSet.getString("SHIP_COUNTRY"));
				client.setShipPostalCode(String.valueOf(resultSet
						.getString("SHIP_POSTALCODE")));
				client.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
				client.setCellNumber(resultSet.getString("CELL_NUMBER"));
				client.setEmail(resultSet.getString("EMAIL"));
				client.setLastGenre(resultSet.getString("LAST_GENRE"));

				results.add(client);
			}
		}

		return results;
	}

	@Override
	public ClientBean searchClient(String username) throws SQLException {

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		String query = "SELECT * FROM CLIENTS WHERE USERNAME = ?";
		username = username.toLowerCase();

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(query);) {

			pStatement.setString(1, username);

			try (ResultSet resultSet = pStatement.executeQuery()) {

				if (resultSet.next()) {
					ClientBean client = new ClientBean();
					client.setId(resultSet.getLong("CLIENT_ID"));
					client.setUsername(resultSet.getString("USERNAME"));
					client.setPassword(resultSet.getString("PASSWORD"));
					client.setManager(resultSet.getBoolean("IS_MANAGER"));
					client.setTitle(resultSet.getString("TITLE"));
					client.setLastName(resultSet.getString("LASTNAME"));
					client.setFirstName(resultSet.getString("FIRSTNAME"));
					client.setCompanyName(resultSet.getString("COMPANY_NAME"));
					client.setAddress1(resultSet.getString("ADDRESS1"));
					client.setAddress2(resultSet.getString("ADDRESS2"));
					client.setCity(resultSet.getString("CITY"));
					client.setProvince(resultSet.getString("PROVINCE"));
					client.setCountry(resultSet.getString("COUNTRY"));
					client.setPostalCode(resultSet.getString("POSTAL_CODE"));
					client.setShipTitle(resultSet.getString("SHIP_TITLE"));
					client.setShipLastName(resultSet
							.getString("SHIP_LAST_NAME"));
					client.setShipFirstName(resultSet
							.getString("SHIP_FIRST_NAME"));
					client.setShipCompanyName(resultSet
							.getString("SHIP_COMPANY_NAME"));
					client.setShipAddress1(resultSet.getString("SHIP_ADDRESS1"));
					client.setShipAddress1(resultSet.getString("SHIP_ADDRESS2"));
					client.setShipCity(resultSet.getString("SHIP_CITY"));
					client.setShipProvince(resultSet.getString("SHIP_PROVINCE"));
					client.setShipCountry(resultSet.getString("SHIP_COUNTRY"));
					client.setShipPostalCode(resultSet
							.getString("SHIP_POSTALCODE"));
					client.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
					client.setCellNumber(resultSet.getString("CELL_NUMBER"));
					client.setEmail(resultSet.getString("EMAIL"));
					client.setLastGenre(resultSet.getString("LAST_GENRE"));

					return client;
				}
			}
		}

		return null;
	}

	@Override
	public ArrayList<ClientBean> searchClients() throws SQLException {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		search = request.getParameter("search");
		type = request.getParameter("by");

		if (type.trim().equalsIgnoreCase("firstname")) {
			clientList = searchClientsByField(search, type);
		} else if (type.trim().equalsIgnoreCase("lastname")) {
			clientList = searchClientsByField(search, type);
		} else if (type.trim().equalsIgnoreCase("company")) {
			clientList = searchClientsByField(search, type);
		} else if (type.trim().equalsIgnoreCase("city")) {
			clientList = searchClientsByField(search, type);
		} else if (type.trim().equalsIgnoreCase("country")) {
			clientList = searchClientsByField(search, type);
		}

		return clientList;
	}

	@Override
	public ArrayList<ClientBean> searchClientsByField(String field, String type)
			throws SQLException {

		ArrayList<ClientBean> clients = new ArrayList<ClientBean>();

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
			} else if (type.equals("name")) {
				ps.setString(1, field);
				ps.setString(2, field);
			} else
				ps.setString(1, field);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					ClientBean client = new ClientBean();

					client.setId(resultSet.getLong("CLIENT_ID"));
					client.setUsername(resultSet.getString("USERNAME"));
					client.setPassword(resultSet.getString("PASSWORD"));
					client.setManager(resultSet.getBoolean("IS_MANAGER"));
					client.setTitle(resultSet.getString("TITLE"));
					client.setLastName(resultSet.getString("LASTNAME"));
					client.setFirstName(resultSet.getString("FIRSTNAME"));
					client.setCompanyName(resultSet.getString("COMPANY_NAME"));
					client.setAddress1(resultSet.getString("ADDRESS1"));
					client.setAddress2(resultSet.getString("ADDRESS2"));
					client.setCity(resultSet.getString("CITY"));
					client.setProvince(resultSet.getString("PROVINCE"));
					client.setCountry(resultSet.getString("COUNTRY"));
					client.setPostalCode(resultSet.getString("POSTAL_CODE"));
					client.setShipTitle(resultSet.getString("SHIP_TITLE"));
					client.setShipLastName(resultSet
							.getString("SHIP_LAST_NAME"));
					client.setShipFirstName(resultSet
							.getString("SHIP_FIRST_NAME"));
					client.setShipCompanyName(resultSet
							.getString("SHIP_COMPANY_NAME"));
					client.setShipAddress1(resultSet.getString("SHIP_ADDRESS1"));
					client.setShipAddress1(resultSet.getString("SHIP_ADDRESS2"));
					client.setShipCity(resultSet.getString("SHIP_CITY"));
					client.setProvince(resultSet.getString("SHIP_PROVINCE"));
					client.setShipCountry(resultSet.getString("SHIP_COUNTRY"));
					client.setShipPostalCode(String.valueOf(resultSet
							.getString("SHIP_POSTALCODE")));
					client.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
					client.setCellNumber(resultSet.getString("CELL_NUMBER"));
					client.setEmail(resultSet.getString("EMAIL"));
					client.setLastGenre(resultSet.getString("LAST_GENRE"));

					// add to clients list
					clients.add(client);
				}
			}
		} catch (NumberFormatException er) {
			return clients;
		}
		return clients;
	}

	private String createSQL(String field) {
		String preparedQuery = "SELECT * FROM clients";
		String whereClause = " WHERE";
		String initialWhereClause = whereClause;

		switch (field) {
		case "firstname":
			whereClause += " FIRSTNAME = ?";
			break;
		case "lastname":
			whereClause += " LASTNAME = ?";
			break;
		case "company":
			whereClause += " COMPANY = ?";
			break;
		case "city":
			whereClause += " CITY = ?";
			break;
		case "country":
			whereClause += " COUNTRY = ?";
			break;
		case "id":
			whereClause += " CLIENT_ID = ?";
			break;
		case "username":
			whereClause += " USERNAME = ?";
			break;
		case "name":
			whereClause += " FIRSTNAME = ? OR LASTNAME = ? ";
			break;
		}

		if (!whereClause.equals(initialWhereClause)) {
			preparedQuery += whereClause;
		}

		return preparedQuery;
	}

	public boolean checkUniqueUsernameOrEmail(String field) throws SQLException {

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		String query = "SELECT * FROM CLIENTS WHERE USERNAME = ? OR EMAIL = ?";
		field = field.toLowerCase();

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(query);) {
			pStatement.setString(1, field);
			pStatement.setString(2, field);
			try (ResultSet resultSet = pStatement.executeQuery()) {
				if (resultSet.next())
					return false;
				else
					return true;
			}
		}
	}

	/**
	 * Allow us to get a list of clients, which will depends of some parameters,
	 * actually store in the url: search = then entry of the user type = by: ID,
	 * Username, name (which do the search for either the lastname and the
	 * firstname)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<TotalSalesBean> getSalesWithClientsList()
			throws SQLException {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		search = request.getParameter("search");
		type = request.getParameter("by");

		if (search == null || type == null) {
			return null;
		}

		if (clientList.size() != 0 || clientList != null)
			clientList.clear();

		if (type.trim().equalsIgnoreCase("id")) {
			clientList = searchClientsByField(search, "id");
		}

		if (type.trim().equalsIgnoreCase("username")) {
			clientList = searchClientsByField(search, "username");
		}

		if (type.trim().equalsIgnoreCase("name")) {
			clientList = searchClientsByField(search, "name");
		}

		ArrayList<TotalSalesBean> list = new ArrayList<>();

		for (ClientBean cb : clientList) {
			list.addAll(rdi.reportSalesByClient(report.getCheckin(),
					report.getCheckout(), cb.getId()));
		}

		return list;
	}

	/**
	 * Retrieves a list of clients based on the search query string and the
	 * type. Search may be filtered based on: ID, First Name, Last Name and User
	 * Name.
	 * 
	 * @return List of clients
	 * @throws SQLException
	 */
	public ArrayList<ClientBean> getClientList() throws SQLException {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		search = request.getParameter("search");
		type = request.getParameter("by");

		if (search != null && type != null) {
			if (clientList.size() != 0 || clientList != null)
				clientList.clear();

			if (type.trim().equalsIgnoreCase("id")) {
				clientList = searchClientsByField(search, "id");
			} else if (type.trim().equalsIgnoreCase("first name")) {
				clientList = searchClientsByField(search, "firstname");
			} else if (type.trim().equalsIgnoreCase("last name")) {
				clientList = searchClientsByField(search, "lastname");
			} else if (type.trim().equalsIgnoreCase("user name")) {
				clientList = searchClientsByField(search, "username");
			} else {
				clientList = getAllClients();
			}
		}
		System.out.println(clientList.size());
		return clientList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientList == null) ? 0 : clientList.hashCode());
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
		ClientDAOImpl other = (ClientDAOImpl) obj;
		if (clientList == null) {
			if (other.clientList != null)
				return false;
		} else if (!clientList.equals(other.clientList))
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
