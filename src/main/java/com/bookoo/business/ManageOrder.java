package com.bookoo.business;

import java.io.Serializable;
import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.bookoo.data.InvoiceBean;
import com.bookoo.data.InvoiceDetailsBean;
import com.bookoo.persistence.implementation.InvoiceDAOImpl;
import com.bookoo.persistence.implementation.InvoiceDetailsDAOImpl;

/**
 * Allow us to set an order available or not, and to get the details of an order.
 * 
 * @author Jolan, Darrel
 * 
 */
@Named("manageOrder")
@RequestScoped
public class ManageOrder implements Serializable {
	private static final long serialVersionUID = -7320442741380563837L;
	
	@Inject
	InvoiceBean ib;
	@Inject
	InvoiceDAOImpl ibi;
	@Inject
	InvoiceDetailsBean invoiceDetails;
	@Inject
	InvoiceDetailsDAOImpl idbi;

	public ManageOrder() {
		super();
	}

	public InvoiceDetailsBean getInvoiceDetails() {
		return invoiceDetails;
	}

	/**
	 * Why a separate class just for this method? - Darrel
	 * @param ibTemp
	 * @return
	 * @throws SQLException
	 */
	public String approuved(InvoiceBean ibTemp) throws SQLException {
		if (ibTemp.isAvailable()) {
			ibTemp.setAvailable(false);
			
		} else {
			ibTemp.setAvailable(true);
		}
		
		// Set availability to false for an invoice and all its items
		int update = ibi.updateInvoice(ibTemp);
		int update2 = ibi.updateDetails(ibTemp);
		
		if (update == 1 && update2 == 1)
			return ("Update succed");
		
		return ("Update fail");
	}

}
