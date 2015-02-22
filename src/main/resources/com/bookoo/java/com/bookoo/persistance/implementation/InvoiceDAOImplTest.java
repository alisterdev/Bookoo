package com.bookoo.persistance.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.bookoo.data.BookBean;
import com.bookoo.data.InvoiceBean;
import com.bookoo.persistence.implementation.InvoiceDAOImpl;
/**
 * Test for InvoiceDAOImpl
 * 
 * @author Jolan Cornevin, Darrel-Day Guerrero
 * @version 1.0
 */
@RunWith(Arquillian.class)
public class InvoiceDAOImplTest {
	
	@Inject
	private InvoiceDAOImpl ik;
	
	@Inject
	private InvoiceBean invoiceBean;
	
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
				.addPackage(InvoiceDAOImpl.class.getPackage())
				.addPackage(InvoiceBean.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				//.addAsResource("createInvoice.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Before
	public void setUp() {
		invoiceBean = new InvoiceBean();
		ik = new InvoiceDAOImpl();
	}
	
	private InvoiceBean fillInvoice(){

		java.util.Date date= new java.util.Date();
		InvoiceBean ivb = new InvoiceBean();
		
		ivb.setSaleDate(new Timestamp(date.getTime()) );
		ivb.setClientId(2);
		ivb.setNetPrice( BigDecimal.valueOf(15.0));
		ivb.setPST( BigDecimal.valueOf(15.0));
		ivb.setGST( BigDecimal.valueOf(15.0));
		ivb.setHST( BigDecimal.valueOf(15.0));
		ivb.setGrossPrice( BigDecimal.valueOf(15.0));
		ivb.setAvailable( true );
		
		return ivb;
	}
	
	@Test
	public void insertBookBean() throws SQLException {
		invoiceBean = fillInvoice();
		
		int results = ik.insertInvoice(invoiceBean);

		assertEquals("Expected 1 results to be inserted", 1, results);
	}
	
	@Test(expected = NullPointerException.class)
	public void insertInvalidClientBean() throws SQLException {
		InvoiceBean test = null;
		ik.insertInvoice(test);
		fail("cannot insert null bean");
	}
	
	
	@Test
	public void updateInvalidInvoiceBean() throws SQLException {
		invoiceBean = new InvoiceBean();
		if(ik.updateInvoice(invoiceBean)!=0) fail("Can't update a null bean");
	}
	
	@Test
	public void updateNonexistentIdInvoiceBean() throws SQLException {
		invoiceBean = fillInvoice();
		
		invoiceBean.setSaleId(10000);
		
		if(ik.updateInvoice(invoiceBean)!=0) fail("Shouldn't update anything");
	}
	
	@Test
	public void removeInvoiceBean() throws SQLException {
		invoiceBean.setSaleId(1);
		
		int results = ik.removeInvoice(invoiceBean);
		
		assertEquals("Expected 1 results to be inserted", 1, results);
	}
	
	@Test
	public void removeInvalideInvoiceBean() throws SQLException {
		invoiceBean= new InvoiceBean();
		if(ik.removeInvoice(invoiceBean)!=0) fail("Can't remove a bean with an invalide id ");
	}
	
	@Test
	public void removeNonexistentInvoiceBean() throws SQLException {
		invoiceBean = fillInvoice();
		invoiceBean.setSaleId(1000);
		
		if(ik.removeInvoice(invoiceBean)!=0) fail("Shouldn't remove anything") ;
	}
	
	@Test
	public void getAllInvoicesBean() throws SQLException{
		ArrayList<InvoiceBean> test = ik.getAllInvoices();
		
		if(test==null){
			fail("Unexcepted null");
		}
		if(test.isEmpty()){
			fail("Unexcepted empty");
		}
	}
	
	@Test
	public void getAllInvoicesByNonexistentClientId() throws Exception {
		long lg= 10000;
		
		ArrayList<InvoiceBean> test = ik.getAllInvoicesByClientId(lg);
		
		if(test==null){
			throw new Exception("Unexcepted null");
		}
		
		if(!test.isEmpty()){
			fail("The arrayList should be empty");
		}
	}
	
	@Test
	public void getInvoiceById() throws Exception {
		long lg= 1;
		
		InvoiceBean test = ik.getInvoiceById(lg);
		
		if(test==null){
			throw new Exception("Unexcepted null");
		}
	}
	
	@Test
	public void getInvoiceByNonexistentId() throws Exception {
		long lg= 1000;
		
		InvoiceBean test = ik.getInvoiceById(lg);
		
		if(test==null){
			throw new Exception("Unexcepted null");
		}
		
		assertEquals(0, test.getSaleId());
	}
	
	@Test
	public void getAllInvoicesByDate() throws Exception {
		java.util.Date date= new java.util.Date();
		Timestamp test = new Timestamp(date.getTime());
		
		ArrayList<InvoiceBean> query = ik.getAllInvoicesByDate(test);
		
		if(query==null){
			throw new Exception("Unexcepted null");
		}
	}
	
}
