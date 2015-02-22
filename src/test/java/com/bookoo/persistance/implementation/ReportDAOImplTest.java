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
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bookoo.data.reports.TopClientBean;
import com.bookoo.data.reports.TopSellerBean;
import com.bookoo.data.reports.TotalSalesBean;
import com.bookoo.data.reports.ZeroSalesBean;
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
				// .addAsResource("createReports.sql")
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

		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 01, 01);
		end.set(2014, 01, 06);

		BigDecimal[][] test = tbi.reportTotalSalesSummary(start, end);
		assertEquals(3, test.length);

	}

	@Test
	public void reportTotalSalesSummary_InvalidDate() throws SQLException {

		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 8, 01);
		end.set(2014, 9, 06);

		BigDecimal[][] test = tbi.reportTotalSalesSummary(start, end);
		assertEquals(3, test.length);

	}

	@Test(expected = NullPointerException.class)
	public void reportTotalSalesSummary_NullDate() throws SQLException {
		tbi.reportTotalSalesSummary(null, null);
	}

	@Test
	public void reportTotalSales() throws SQLException {

		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 06);

		ArrayList<TotalSalesBean> test = tbi.reportTotalSales(start, end);
		assertEquals(0, test);
	}

	@Test
	public void reportTotalSales_InvalidDate() throws SQLException {
		ArrayList<TotalSalesBean> test;

		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 8, 01);
		end.set(2014, 9, 06);

		test = tbi.reportTotalSales(start, end);

		if (test.size() != 0) {
			fail("the size should be equals to 0");
		}
	}

	@Test(expected = NullPointerException.class)
	public void reportTotalSales_NullDate() throws SQLException {
		tbi.reportTotalSales(null, null);
	}

	@Test
	public void reportSalesByClientSummary() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 01, 01);
		end.set(2014, 01, 06);

		BigDecimal[][] test = tbi.reportSalesByClientSummary(start, end,
				Long.valueOf(1));
		assertEquals(3, test.length);
	}

	@Test
	public void reportSalesByClientSummary_InvalidClientId()
			throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 10, 06);

		BigDecimal[][] test = tbi.reportSalesByClientSummary(start, end,
				Long.valueOf(1000));
		assertEquals(3, test.length);
	}

	@Test
	public void reportSalesByClientSummary_InvalidDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 9, 01);
		end.set(2014, 10, 06);

		BigDecimal[][] test = tbi.reportSalesByClientSummary(start, end,
				Long.valueOf(1));
		assertEquals(3, test.length);
	}

	@Test(expected = NullPointerException.class)
	public void reportSalesByClientSummary_NullDate() throws SQLException {
		tbi.reportSalesByClientSummary(null, null, Long.valueOf(1));
	}

	@Test
	public void reportSalesByClient() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 06);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByClient(start, end,
				Long.valueOf(1));
		assertTrue(test.size() > 0);
	}

	@Test
	public void reportSalesByClient_InvalidDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 6, 01);
		end.set(2014, 7, 06);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByClient(start, end,
				Long.valueOf(1));

		if (test.size() != 0) {
			fail("the size should be equals to 0");
		}
	}

	@Test(expected = NullPointerException.class)
	public void reportSalesByClient_NullDate() throws SQLException {
		tbi.reportSalesByClient(null, null, Long.valueOf(1));
	}

	@Test
	public void reportSalesByClient_InvalidClientId() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 6, 01);
		end.set(2014, 7, 06);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByClient(start, end,
				Long.valueOf(100));

		if (test.size() != 0) {
			fail("the size should be equals to 0");
		}
	}

	@Test
	public void reportSalesByAuthorSummary_InvalidAuthorName()
			throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 06);

		BigDecimal[][] test = tbi.reportSalesByAuthorSummary(start, end,
				"Leoline Tolstoy");

		assertTrue(test.length != 3);
	}

	@Test
	public void reportSalesByAuthorSummary_InvalidDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 9, 01);
		end.set(2014, 8, 06);

		BigDecimal[][] test = tbi.reportSalesByAuthorSummary(start, end,
				"Hommer");

		assertTrue(test.length != 3);
	}

	@Test(expected = NullPointerException.class)
	public void reportSalesByAuthorSummary_NullDate() throws SQLException {
		tbi.reportSalesByAuthorSummary(null, null, "_");
	}

	@Test
	public void reportSalesByAuthorSummary() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 30);

		BigDecimal[][] test = tbi.reportSalesByAuthorSummary(start, end,
				"Hommer");

		assertTrue(test.length != 3);
	}

	@Test
	public void reportSalesByAuthor() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);

		end.set(2014, 1, 30);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByAuthor(start, end,
				"author");

		assertTrue(test.size() == 0);
	}

	@Test
	public void reportSalesByAuthor_InvalidDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 8, 01);
		end.set(2014, 9, 06);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByAuthor(start, end,
				"Leo Tolstoy");

		assertTrue(test.size() != 0);

	}

	@Test(expected = NullPointerException.class)
	public void reportSalesByAuthor_NullDate() throws SQLException {
		tbi.reportSalesByAuthor(null, null, "_");
		fail("Exception was expected but not thrown!");
	}

	@Test
	public void reportSalesByAuthor_InvalidAuthorName() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 06);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByAuthor(start, end,
				"Leoline Tolstoy");

		assertTrue(test.size() != 0);

	}

	@Test
	public void reportSalesByPublisherSummary() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);

		end.set(2015, 1, 30);

		BigDecimal[][] test = tbi.reportSalesByPublisherSummary(start, end,
				"publisher");

		assertTrue(test.length != 3);

	}

	@Test
	public void reportSalesByPublisherSummary_InvalidDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 8, 01);
		end.set(2014, 9, 06);

		BigDecimal[][] test = tbi.reportSalesByPublisherSummary(start, end,
				"Penguin UK");

		assertTrue(test.length != 3);

	}

	@Test(expected = NullPointerException.class)
	public void reportSalesByPublisherSummary_NullDate() throws SQLException {
		tbi.reportSalesByPublisherSummary(null, null, "_");
		fail("Exception was expected but not thrown!");
	}

	@Test
	public void reportSalesByPublisherSummary_InvalidPublisherName()
			throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 06);

		BigDecimal[][] test = tbi.reportSalesByPublisherSummary(start, end,
				"publisher2");

		assertTrue(test.length != 3);

	}

	@Test
	public void reportSalesByPublisher() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 30);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByPublisher(start, end,
				"publisher");

		assertTrue(test.size() == 0);
	}

	@Test
	public void reportSalesByPublisher_InvalidDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 7, 01);
		end.set(2014, 8, 06);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByPublisher(start, end,
				"Penguin UK");

		assertTrue(test.size() != 0);

	}

	@Test(expected = NullPointerException.class)
	public void reportSalesByPublisher_NullDate() throws SQLException {
		tbi.reportSalesByPublisher(null, null, "_");
		fail("Exception was expected but not thrown");
	}

	@Test
	public void reportSalesByPublisher_InvalidPublisherName()
			throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 7, 01);
		end.set(2014, 8, 06);

		ArrayList<TotalSalesBean> test = tbi.reportSalesByPublisher(start, end,
				"Penguin CA");

		assertTrue(test.size() != 0);

	}

	@Test
	public void reportTopSellers() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 06);

		List<TopSellerBean> test = tbi.reportTopSellers(start, end);

		assertTrue(test.size() == 0);
	}

	@Test
	public void reportTopSellers_InvalideDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 8, 01);
		end.set(2014, 6, 06);

		List<TopSellerBean> test = tbi.reportTopSellers(start, end);

		assertTrue(test.size() != 0);
	}

	@Test(expected = NullPointerException.class)
	public void reportTopSellers_NullDate() throws SQLException {
		tbi.reportTopSellers(null, null);
		fail("Exception was expected but not thrown");
	}

	@Test
	public void reportTopClients() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 06);

		List<TopClientBean> test = tbi.reportTopClients(start, end);

		assertTrue(test.size() != 0);

	}

	@Test
	public void reportTopClients_InvalideDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 8, 01);
		end.set(2014, 6, 06);

		List<TopClientBean> test = tbi.reportTopClients(start, end);

		assertTrue(test.size() != 0);

	}

	@Test(expected = NullPointerException.class)
	public void reportTopClients_NullDate() throws SQLException {
		tbi.reportTopClients(null, null);
		fail("Exception was expected but not thrown");
	}

	@Test
	public void reportUnsoldBooks() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 1, 01);
		end.set(2014, 1, 30);

		List<ZeroSalesBean> test = tbi.reportUnsoldBooks(start, end);

		assertTrue(test.size() != 0);

	}

	@Test
	public void reportUnsoldBooks_InvalideDate() throws SQLException {
		Calendar start = new GregorianCalendar();
		Calendar end = new GregorianCalendar();

		start.set(2014, 8, 01);
		end.set(2014, 6, 06);

		List<ZeroSalesBean> test = tbi.reportUnsoldBooks(start, end);
		assertTrue(test.size() != 0);

	}

	@Test(expected = NullPointerException.class)
	public void reportUnsoldBooks_NullDate() throws SQLException {
		tbi.reportUnsoldBooks(null, null);
		fail("Exception was expected but not thrown");
	}

}
