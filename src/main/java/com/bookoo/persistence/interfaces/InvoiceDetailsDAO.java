package com.bookoo.persistence.interfaces;

import com.bookoo.data.CartItemBean;
import com.bookoo.data.InvoiceBean;
import com.bookoo.data.InvoiceDetailsBean;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access routine interface for the Book data bean
 * 
 * @author Jolan Cornevin, Alex Ilea, Darrel Guerrero
 * @version 1.0
 */
public interface InvoiceDetailsDAO {

	public int addInvoiceItem(CartItemBean item) throws SQLException;

	public int updateInvoiceItem(InvoiceDetailsBean invoice) throws SQLException;

	public int removeInvoiceItem(InvoiceDetailsBean invoice) throws SQLException;

	ArrayList<InvoiceDetailsBean> getInvoiceDetailsBySaleId(InvoiceBean invoice)
			throws SQLException;

	public int updateBookCopies(CartItemBean item) throws SQLException;

}
