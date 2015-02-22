package com.bookoo.persistance.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bookoo.data.BannerBean;
import com.bookoo.data.BookBean;
import com.bookoo.data.reports.TopClientBean;
import com.bookoo.data.reports.TopSellerBean;
import com.bookoo.data.reports.TotalSalesBean;
import com.bookoo.persistence.implementation.ReportsDAOImpl;

/*
 * @author Jolan
 */

@RunWith(Arquillian.class)
public class ReportDAOImplTest {
	
	@Inject
	private ReportsDAOImpl tbi;
	
	@Inject
	private TotalSalesBean totalSales;
	
	@Resource(name = "jdbc/g2w14")
	private DataSource ds;
	
	@Deployment
	public static WebArchive deploy() {

		final File[] dependencies = Maven
				.resolver()
				.loadPomFromFile("pom.xml")
				.resolve("mysql:mysql-connector-java",
						"org.assertj:assertj-core").withoutTransitivity()
				.asFile();

		final WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
				.addPackage(TotalSalesBean.class.getPackage())
				.addPackage(ReportsDAOImpl.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				//.addAsResource("createReports.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Before
	public void setUp() {
		totalSales = new TotalSalesBean();
		
		totalSales.setTitle("hi");
		totalSales.setQuantity(2);
		totalSales.setPrice(BigDecimal.valueOf(12.5));
		totalSales.setTotal(BigDecimal.valueOf(25));
		
		tbi = new ReportsDAOImpl();
	}

	@Test
	public void reportTotalSalesSummary() throws SQLException {
		BigDecimal [] test;
		
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 01, 01);
		end.set(2014, 01, 06);
		
		test = tbi.reportTotalSalesSummary(start, end);
		
		if(test.length!=3){
			fail("the size should be 3");
		}
		if(test[0] == null || test[1] == null || test[2] ==null){
			fail("none of those val should be null");
		}
	}
	
	@Test
	public void reportTotalSalesSummary_InvalidDate() throws SQLException {
		BigDecimal [] test;
		
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 8, 01);
		end.set(2014, 9, 06);
		
		test = tbi.reportTotalSalesSummary(start, end);
		
		if(test.length!=3){
			fail("the size should be 3");
		}
		if(test[0] != null || test[1] != null || test[2] !=null){
			fail("none of those val should be not null");
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void reportTotalSalesSummary_NullDate() throws SQLException {
		tbi.reportTotalSalesSummary(null, null);
	}
	
	@Test
	public void reportTotalSales() throws SQLException {
		ArrayList<TotalSalesBean> test;
		
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 06);
		
		test = tbi.reportTotalSales(start, end);
		
		if(test.size()==0){
			fail("the size should be gretter than 0");
		}
		for(TotalSalesBean tsb : test){
			if(Long.valueOf(tsb.getBookID())==null){
				fail("book id shouldn't be null");
			}
			if((tsb.getTitle())==null){
				fail("Title shouldn't be null");
			}
			if(Integer.valueOf(tsb.getQuantity())==null){
				fail("Quantity shouldn't be null");
			}
			
			if(Long.valueOf(tsb.getPrice().longValue())==null){
				fail("Price shouldn't be null");
			}
			if(Long.valueOf(tsb.getTotal().longValue())==null){
				fail("Total shouldn't be null");
			}
		}
		
	}
	
	@Test
	public void reportTotalSales_InvalidDate() throws SQLException {
		ArrayList<TotalSalesBean> test;
		
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 8, 01);
		end.set(2014, 9, 06);
		
		test = tbi.reportTotalSales(start, end);
		
		if(test.size()!=0){
			fail("the size should be equals to 0");
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void reportTotalSales_NullDate() throws SQLException {
		tbi.reportTotalSales(null, null);
	}
	
	@Test 
	public void reportSalesByClientSummary() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 01, 01);
		end.set(2014, 01, 06);
		
		BigDecimal[] test = tbi.reportSalesByClientSummary(start, end, Long.valueOf(1) );
		
		if(test.length!=3){
			fail("the size should be 3");
		}
		if(test[0] == null || test[1] == null || test[2] ==null){
			fail("none of those val should be null");
		}
	}
	
	@Test 
	public void reportSalesByClientSummary_InvalidClientId() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 10, 06);
		
		BigDecimal[] test = tbi.reportSalesByClientSummary(start, end, Long.valueOf(1000) );
		
		if(test.length!=3){
			fail("the size should be 3");
		}
		if(test[0] != null || test[1] != null || test[2] !=null){
			fail("none of those val should be not be null");
		}
	}
	
	@Test 
	public void reportSalesByClientSummary_InvalidDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 9, 01);
		end.set(2014, 10, 06);
		
		BigDecimal[] test = tbi.reportSalesByClientSummary(start, end, Long.valueOf(1) );
		
		if(test.length!=3){
			fail("the size should be 3");
		}
		if(test[0] != null || test[1] != null || test[2] !=null){
			fail("none of those val should be not be null");
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void reportSalesByClientSummary_NullDate() throws SQLException {
		tbi.reportSalesByClientSummary(null, null, Long.valueOf(1));
	}
	
	@Test
	public void reportSalesByClient() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 06);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByClient(start, end, Long.valueOf(1) );
		
		if(test.size()==0){
			fail("the size should be gretter than 0");
		}
		
		for(TotalSalesBean tsb : test){
			if(Long.valueOf(tsb.getBookID())==null){
				fail("book id shouldn't be null");
			}
			if((tsb.getTitle())==null){
				fail("Title shouldn't be null");
			}
			if(Integer.valueOf(tsb.getQuantity())==null){
				fail("Quantity shouldn't be null");
			}
			
			if(Long.valueOf(tsb.getPrice().longValue())==null){
				fail("Price shouldn't be null");
			}
			if(Long.valueOf(tsb.getTotal().longValue())==null){
				fail("Total shouldn't be null");
			}
		}
	}
	
	@Test
	public void reportSalesByClient_InvalidDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 6, 01);
		end.set(2014, 7, 06);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByClient(start, end, Long.valueOf(1) );
		
		if(test.size()!=0){
			fail("the size should be equals to 0");
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void reportSalesByClient_NullDate() throws SQLException {
		tbi.reportSalesByClient(null, null, Long.valueOf(1));
	}
	
	@Test
	public void reportSalesByClient_InvalidClientId() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 6, 01);
		end.set(2014, 7, 06);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByClient(start, end, Long.valueOf(100) );
		
		if(test.size()!=0){
			fail("the size should be equals to 0");
		}
	}
	
	@Test
	public void reportSalesByAuthorSummary_InvalidAuthorName() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 06);
		
		BigDecimal[] test = tbi.reportSalesByAuthorSummary(start, end, "Leoline Tolstoy" );
		
		if(test.length != 3){
			fail("the size shouldn't be different from 0");
		}
		
		if(test[0] != null || test[1] != null || test[2] !=null){
			fail("none of those val should be null");
		}
	}
	
	@Test
	public void reportSalesByAuthorSummary_InvalidDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 9, 01);
		end.set(2014, 8, 06);
		
		BigDecimal[] test = tbi.reportSalesByAuthorSummary(start, end, "Hommer" );
		
		if(test.length != 3){
			fail("the size shouldn't be different from 0");
		}
		
		if(test[0] != null || test[1] != null || test[2] !=null){
			fail("none of those val should be null");
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void reportSalesByAuthorSummary_NullDate() throws SQLException {
		tbi.reportSalesByAuthorSummary(null, null, "_");
	}
	
	@Test
	public void reportSalesByAuthorSummary() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 30);
		

		BigDecimal[] test = tbi.reportSalesByAuthorSummary(start, end, "Hommer" );

		
		if(test.length != 3){
			fail("the size shouldn't be different from 3");
		}
		
		if(test[0] == null || test[1] == null || test[2] ==null){
			fail("None of these values should be null");
		}
	}
	
	@Test
	public void reportSalesByAuthor() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);

		end.set(2014, 1, 30);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByAuthor(start, end, "author" );

		
		if(test.size()==0){
			fail("the size shouldn't be equal to 0");
		}
		
		for(TotalSalesBean tsb : test){
			if(Long.valueOf(tsb.getBookID())==null){
				fail("book id shouldn't be null");
			}
			if((tsb.getTitle())==null){
				fail("Title shouldn't be null");
			}
			if(Integer.valueOf(tsb.getQuantity())==null){
				fail("Quantity shouldn't be null");
			}
			
			if(Long.valueOf(tsb.getPrice().longValue())==null){
				fail("Price shouldn't be null");
			}
			if(Long.valueOf(tsb.getTotal().longValue())==null){
				fail("Total shouldn't be null");
			}
		}
		
	}
	
	@Test
	public void reportSalesByAuthor_InvalidDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 8, 01);
		end.set(2014, 9, 06);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByAuthor(start, end, "Leo Tolstoy" );
		
		if(test.size()!=0){
			fail("the size should be equal to 0");
		}
		
	}
	
	@Test(expected = NullPointerException.class)
	public void reportSalesByAuthor_NullDate() throws SQLException {
		tbi.reportSalesByAuthor(null, null, "_");
	}
	
	@Test
	public void reportSalesByAuthor_InvalidAuthorName() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 06);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByAuthor(start, end, "Leoline Tolstoy" );
		
		if(test.size()!=0){
			fail("The size should be equal to 0");
		}
		
	}
	
	@Test
	public void reportSalesByPublisherSummary() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);

		end.set(2015, 1, 30);

		BigDecimal[] test = tbi.reportSalesByPublisherSummary(start, end, "publisher" );

		
		if(test.length != 3){
			fail("The size shouldn't be different from 3.");
		}
		
		if(test[0] == null || test[1] == null || test[2] ==null){
			fail("None of those val should be null");
		}
		
	}
	
	@Test
	public void reportSalesByPublisherSummary_InvalidDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 8, 01);
		end.set(2014, 9, 06);
		
		BigDecimal[] test = tbi.reportSalesByPublisherSummary(start, end, "Penguin UK" );
		
		if(test.length != 3){
			fail("the size shouldn't be different from 3");
		}
		
		if(test[0] != null || test[1] != null || test[2] !=null){
			fail("none of those val should be null");
		}
		
	}
	
	@Test(expected = NullPointerException.class)
	public void reportSalesByPublisherSummary_NullDate() throws SQLException {
		tbi.reportSalesByPublisherSummary(null, null, "_");
	}
	
	@Test
	public void reportSalesByPublisherSummary_InvalidPublisherName() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 06);
		
		BigDecimal[] test = tbi.reportSalesByPublisherSummary(start, end, "publisher2" );
		
		if(test.length !=3){
			fail("the size shouldn't be different from 0");
		}
		

		if(test[0] != null || test[1] != null || test[2] !=null){
			fail("None of these values should be null");
		}
	}
	
	@Test
	public void reportSalesByPublisher() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 30);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByPublisher(start, end, "publisher" );

		
		if(test.size()==0){
			fail("the size shouldn't be equal to 0");
		}
		
		for(TotalSalesBean tsb : test){
			if(Long.valueOf(tsb.getBookID())==null){
				fail("book id shouldn't be null");
			}
			if((tsb.getTitle())==null){
				fail("Title shouldn't be null");
			}
			if(Integer.valueOf(tsb.getQuantity())==null){
				fail("Quantity shouldn't be null");
			}
			
			if(Long.valueOf(tsb.getPrice().longValue())==null){
				fail("Price shouldn't be null");
			}
			if(Long.valueOf(tsb.getTotal().longValue())==null){
				fail("Total shouldn't be null");
			}
		}
	}
	
	@Test
	public void reportSalesByPublisher_InvalidDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 7, 01);
		end.set(2014, 8, 06);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByPublisher(start, end, "Penguin UK" );
		
		if(test.size()!=0){
			fail("the size should be equal to 0");
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void reportSalesByPublisher_NullDate() throws SQLException {
		tbi.reportSalesByPublisher(null, null, "_");
	}
	
	@Test
	public void reportSalesByPublisher_InvalidPublisherName() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 7, 01);
		end.set(2014, 8, 06);
		
		ArrayList<TotalSalesBean> test = tbi.reportSalesByPublisher(start, end, "Penguin CA" );
		
		if(test.size()!=0){
			fail("the size should be equal to 0");
		}
	}
	
	@Test
	public void reportTopSellers() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 06);
		
		List<TopSellerBean> test = tbi.reportTopSellers(start, end);
		
		if(test.size()==0){
			fail("the size shouldn't be equals to 0");
		}
		
		for(TopSellerBean tsb : test){
			if(Long.valueOf(tsb.getBookId())==null){
				fail("The book id shouldn't be null");
			}
			if(String.valueOf(tsb.getTitle())==null){
				fail("The book title shouldn't be null");
			}
			if(Integer.valueOf(tsb.getQuantitySold())==null){
				fail("The quantity shouldn't be null");
			}
		}
	}
	
	@Test
	public void reportTopSellers_InvalideDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 8, 01);
		end.set(2014, 6, 06);
		
		List<TopSellerBean> test = tbi.reportTopSellers(start, end);
		
		if(test.size()!=0){
			fail("the size should be equals to 0");
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void reportTopSellers_NullDate() throws SQLException {
		tbi.reportTopSellers(null, null);
	}
	
	@Test
	public void reportTopClients() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 06);
		
		List<TopClientBean> test = tbi.reportTopClients(start, end);
		
		if(test.size()==0){
			fail("the size shouldn't be equals to 0");
		}
		
		for(TopClientBean tsb : test){
			if(Long.valueOf(tsb.getClientID())==null){
				fail("The client ID shouldn't be null");
			}
			if(String.valueOf(tsb.getFirstName())==null){
				fail("The client first name shouldn't be null");
			}
			if(String.valueOf(tsb.getLastName())==null){
				fail("The client last name shouldn't be null");
			}
			if(tsb.getTotal()==null){
				fail("The client last name shouldn't be null");
			}
		}
	}
	
	@Test
	public void reportTopClients_InvalideDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 8, 01);
		end.set(2014, 6, 06);
		
		List<TopClientBean> test = tbi.reportTopClients(start, end);
		
		if(test.size()!=0){
			fail("the size should be equals to 0");
		}		
	}
	
	@Test(expected = NullPointerException.class)
	public void reportTopClients_NullDate() throws SQLException{
		tbi.reportTopClients(null, null);		
	}
	
	@Test
	public void reportUnsoldBooks() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 1, 01);
		end.set(2014, 1, 30);
		
		List<BookBean> test = tbi.reportUnsoldBooks(start, end);
		
		if(test.size()==0){
			fail("the size shouldn't be equals to 0");
		}
		
		for(BookBean tsb : test){
			if(tsb.getIsbn10()==null){
				fail("the ISBN Shouldn't be null");
			}
			if(Long.valueOf(tsb.getId())==null){
				fail("the ID Shouldn't be null");
			}
		}		
	}
	
	@Test
	public void reportUnsoldBooks_InvalideDate() throws SQLException{
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		
		start.set(2014, 8, 01);
		end.set(2014, 6, 06);
		
		List<BookBean> test = tbi.reportUnsoldBooks(start, end);
		
		if(test.size()!=0){
			fail("the size shouldn't be equals to 0");
		}
		
	}
	
	@Test(expected = NullPointerException.class)
	public void reportUnsoldBooks_NullDate() throws SQLException{
		tbi.reportUnsoldBooks(null, null);	
	}
	
	@Test
	public void getLowInventoryBooks() throws SQLException{
		List<BookBean> test = tbi.reportLowInventoryBooks();
		
		if(test.size()==0){
			fail("the size shouldn't be equals to 0");
		}
		
		for(BookBean tsb : test){
			if(tsb.getAuthor()==null){
				fail("the author Shouldn't be null");
			}
			if(Integer.valueOf(tsb.getCopies())==null){
				fail("the copies Shouldn't be null");
			}
			if(Long.valueOf(tsb.getId())==null){
				fail("the id Shouldn't be null");
			}
			if(tsb.getIsbn10()==null){
				fail("the isbn10 Shouldn't be null");
			}
			if(tsb.getIsbn13()==null){
				fail("the isbn13 Shouldn't be null");
			}
			if(tsb.getPublisher()==null){
				fail("the isbn13 Shouldn't be null");
			}
			if(tsb.getTitle()==null){
				fail("the isbn13 Shouldn't be null");
			}
		}
	}
	
	@Test
	public void reportStock() throws SQLException{
		List<BookBean> test = tbi.reportStock();
		
		if(test.size()==0){
			fail("the size shouldn't be equals to 0");
		}
		
		for(BookBean tsb : test){
			if(tsb.isAvailable()==false){
				fail("the availability shouldn't be false");
			}
			if(tsb.getAuthor()==null){
				fail("the author Shouldn't be null");
			}
			if(Integer.valueOf(tsb.getCopies())==null){
				fail("the copies Shouldn't be null");
			}
			if(Long.valueOf(tsb.getId())==null){
				fail("the id Shouldn't be null");
			}
			if(tsb.getIsbn10()==null){
				fail("the isbn10 Shouldn't be null");
			}
			if(tsb.getIsbn13()==null){
				fail("the isbn13 Shouldn't be null");
			}
			if(tsb.getPublisher()==null){
				fail("the isbn13 Shouldn't be null");
			}
			if(tsb.getTitle()==null){
				fail("the isbn13 Shouldn't be null");
			}
		}	
	}

}
