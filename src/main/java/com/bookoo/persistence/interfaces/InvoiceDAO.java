package com.bookoo.persistence.interfaces;

import com.bookoo.data.InvoiceBean;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Data Access routine interface for the Book data bean
 * 
 * @author Jolan Cornevin, Anna Rogozin
 * @version 1.0
 */
public interface InvoiceDAO {

	public int insertInvoice()
			throws SQLException;

	public int updateInvoice(InvoiceBean invoice)
			throws SQLException;

	public int removeInvoice(InvoiceBean invoice)
			throws SQLException;

	public ArrayList<InvoiceBean> getAllInvoices()
			throws SQLException;

	public ArrayList<InvoiceBean> getAllInvoicesByClientId (long id)
			throws SQLException;

	public InvoiceBean getInvoiceById (long id)
			throws SQLException;

	public ArrayList<InvoiceBean> getAllInvoicesByDate (Timestamp date)
			throws SQLException;

	public ArrayList<InvoiceBean> getInvoiceList() throws SQLException;

	public ArrayList<InvoiceBean> searchInvoiceByField(String field, String type)
			throws SQLException;

	public int updateDetails(InvoiceBean invoice) throws SQLException;

}
