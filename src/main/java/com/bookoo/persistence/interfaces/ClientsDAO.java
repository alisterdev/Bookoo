package com.bookoo.persistence.interfaces;


import com.bookoo.data.ClientBean;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access routine interface for the Client Data Bean
 * 
 * @author Darrel-Day Guerrero
 * @version 1.0
 */
public interface ClientsDAO {

	// Management Side (Back End)
	
	/**
	 * Search client(s) with total value of purchases
	 * 
	 * @param client
	 * @return
	 * @throws SQLException
	 */
	public ClientBean searchClient(String username) throws SQLException;
	
	/**
	 * Search client(s) with total value of purchases
	 * 
	 * @param client
	 * @return
	 * @throws SQLException
	 */
	public  ArrayList<ClientBean> searchClients() throws SQLException;
	
	/**
	 * Search client(s) by field with total value of purchases
	 * @param field
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<ClientBean> searchClientsByField(String field, String type) throws SQLException;
	

	/**
	 * Search all clients 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<ClientBean> getAllClients() throws SQLException;
	
	// User Side (Front End)
	
	/**
	 * Add a client record
	 *
	 * @return
	 * @throws SQLException
	 */
	public int insertClientRecord() throws SQLException;

	/**
	 * Update user profile
	 * 
	 * @param formClient
	 * @return
	 * @throws SQLException
	 */
	public int updateUserProfile(ClientBean formClient) throws SQLException;


}
