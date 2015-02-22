/**
 * 
 */
package com.bookoo.persistance.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

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
import com.bookoo.data.InvoiceDetailsBean;
import com.bookoo.persistence.implementation.InvoiceDetailsDOAImpl;

/**
 * @author Sihem
 *
 */
@RunWith(Arquillian.class)
public class InvoiceDetailsDAOImplTest {

	private InvoiceDetailsDOAImpl iddi;
	
	@Inject
	private InvoiceDetailsBean invoiceDetails;
	
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
				.addPackage(BannerBean.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				.addAsResource("createInvoiceDetails.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Before
	public void setUp(){
		invoiceDetails = new InvoiceDetailsBean();
		iddi = new InvoiceDetailsDOAImpl();
	}
	

	@Test
	public void testAddInvoiceItem() throws SQLException {
		
		invoiceDetails.setSaleId(1);
		invoiceDetails.setQuantity(2);
		invoiceDetails.setBookId(1);
		invoiceDetails.setBookPrice(BigDecimal.valueOf(10.0));
		invoiceDetails.setAvailable(true);
		
		int results = iddi.addInvoiceItem(invoiceDetails);
		assertEquals("Expected 1 results to be inserted", 1, results);
	}
	
	@Test(expected = SQLException.class)
	public void testAddInvalidInvoiceItem() throws SQLException {
		iddi.addInvoiceItem(invoiceDetails);
		fail("Can't insert a null bean");
	}
	
	@Test
	public void testUpdateInvoiceItem() throws SQLException {
		
		invoiceDetails.setSaleId(1);
		invoiceDetails.setQuantity(2);
		invoiceDetails.setBookId(1);
		invoiceDetails.setBookPrice(BigDecimal.valueOf(10.0));
		invoiceDetails.setAvailable(true);
		
		invoiceDetails.setDetailsId(1);
		
		int results = iddi.updateInvoiceItem(invoiceDetails);
		assertEquals("Expected 1 results to be updated", 1, results);
	}
	
	@Test
	public void testUpdateInvalidInvoiceItem() throws SQLException {
		int results = iddi.updateInvoiceItem(invoiceDetails);
		assertEquals(0, results);
	}
	
	@Test
	public void testGetAllBanners() throws SQLException{
		
		ArrayList<InvoiceDetailsBean> results = new ArrayList<InvoiceDetailsBean>();
		results = iddi.getAll(invoiceDetails);
		
		if(results==null){
			fail("Unexcepted null");
		}
		
		System.out.println(results);
		
	}
	
	@Test
	public void testRemoveInvoiceItem() throws SQLException {
		
		invoiceDetails.setDetailsId(2);
		
		int results = iddi.removeInvoiceItem(invoiceDetails);
		assertEquals(1, results);
	}

}
