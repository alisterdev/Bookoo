package com.bookoo.persistence.interfaces;

import com.bookoo.data.BookBean;
import com.bookoo.data.ClientBean;
import com.bookoo.data.reports.TopClientBean;
import com.bookoo.data.reports.TopSellerBean;
import com.bookoo.data.reports.TotalSalesBean;
import com.bookoo.data.reports.ZeroSalesBean;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Data Access routine interface for reports
 * 
 * @author Darrel-Day Guerrero, Anna Rogozin
 * @version 1.0
 */
public interface ReportsDAO {

	// Management side (Back End)

	// Total Sales
	public BigDecimal[][] reportTotalSalesSummary(Calendar start, Calendar end) throws SQLException;
	public ArrayList<TotalSalesBean> reportTotalSales(Calendar start, Calendar end) throws SQLException;

	// Sales by Client
	public BigDecimal[][] reportSalesByClientSummary(Calendar start,
			Calendar end, long clientId) throws SQLException;
	
	public ArrayList<TotalSalesBean> reportSalesByClient(Calendar start,
			Calendar end, long clientId) throws SQLException;

	// Sales by Author
	public BigDecimal[][] reportSalesByAuthorSummary(Calendar start,
			Calendar end, String authorName) throws SQLException;

	public ArrayList<TotalSalesBean> reportSalesByAuthor(Calendar start,
			Calendar end, String authorName) throws SQLException;

	// Sales by Publisher
	public BigDecimal[][] reportSalesByPublisherSummary(Calendar start,
			Calendar end, String publisherName) throws SQLException;

	public ArrayList<TotalSalesBean> reportSalesByPublisher(Calendar start,
			Calendar end, String publisherName) throws SQLException;

	// Top Clients
	public List<TopClientBean> reportTopClients(Calendar start, Calendar end) throws SQLException;

	// Zero Sales
	public List<ZeroSalesBean> reportUnsoldBooks(Calendar start, Calendar end) throws SQLException;

	// Reorder Report
	public List<BookBean> getLowInventoryReport() throws SQLException;

	// Stock Report
	public List<BookBean> getInventoryReport() throws SQLException;
	
	public BigDecimal[][] getInventoryValue() throws SQLException;
	
	ArrayList<TopSellerBean> reportTopSellers(Calendar start, Calendar end)
			throws SQLException;
	
	// Client Management Total Sales
	public BigDecimal getClientValue(ClientBean client)
			throws SQLException;
	

}