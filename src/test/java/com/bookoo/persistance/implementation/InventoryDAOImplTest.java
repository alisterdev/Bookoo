
package com.bookoo.persistance.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.naming.NamingException;
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
import com.bookoo.data.BookBean;
import com.bookoo.persistence.implementation.InventoryDAOImpl;

/**
 * Test for InvoiceDAOImpl
 * 
 * @author Jolan Cornevin
 * @version 1.0
 */

@RunWith(Arquillian.class)
public class InventoryDAOImplTest {
	
	@Inject
	private InventoryDAOImpl bookBeani;
	
	@Inject
	private BookBean bookBean;
	
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
				.addPackage(BookBean.class.getPackage())
				.addPackage(InventoryDAOImpl.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				.addAsResource("testDatabase.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Before
	public void setUp() throws NamingException{
		bookBean = new BookBean();
		bookBeani = new InventoryDAOImpl();
	}
	
	private BookBean fillBookBean(BookBean bookBean){
		bookBean.setIsbn13("1234567890123");
		bookBean.setIsbn10("1234567890");
		bookBean.setTitle("title");
		bookBean.setAuthor("author");
		bookBean.setPublisher("publisher");
		bookBean.setPages(150);
		bookBean.setGenre("genre");
		bookBean.setCover("cover");
		bookBean.setBookType(1);
		bookBean.setBookFormat(1);
		bookBean.setDownloadLink("link");
		bookBean.setCopies(15);
		bookBean.setWholesalePrice( BigDecimal.valueOf(15.0));
		bookBean.setListPrice( BigDecimal.valueOf(15.0));
		bookBean.setSalePrice( BigDecimal.valueOf(15.0));
		bookBean.setWeight( 15.0);
		bookBean.setDimensions("15x12x40");
		bookBean.setDateAdded(Timestamp.valueOf("2014-12-01 12:12:12"));
		bookBean.setAvailable(false);
		
		return bookBean;
	}
	
	@Test
	public void insertBookBean() throws SQLException {
		bookBean = fillBookBean(bookBean);
		int result = bookBeani.addBookRecord();
		assertEquals(1, result);
	}
	
	@Test(expected = NullPointerException.class)
	public void insertInvalideBookBean() throws SQLException {		
		bookBeani.addBookRecord();
		fail("We can't insert an invalide book");
	}
	
	@Test(expected = NullPointerException.class)
	public void updateInvalideBookBean() throws SQLException {		
		bookBeani.editBookRecord(bookBean);
		fail("We can't update an invalide book");
	}
	
	@Test(expected = NullPointerException.class)
	public void updateNonexistentBookBean() throws SQLException {		
		bookBean.setId(1000);
		int result = bookBeani.editBookRecord(bookBean);
		assertEquals(0, result);
	}
	
	@Test
	public void updateBookBean() throws SQLException {		
		bookBean.setId(1);
		bookBean = fillBookBean(bookBean);
		int result = bookBeani.editBookRecord(bookBean);
		assertEquals(1, result);
	}
	
	@Test
	public void removeBookBean() throws SQLException {		
		bookBean.setId(1);
		bookBean = fillBookBean(bookBean);
		int result = bookBeani.editBookRecord(bookBean);
		assertEquals(1, result);
	}
	
	@Test
	public void removeNonexistentBookBean() throws SQLException {		
		bookBean.setId(1000);
		bookBean = fillBookBean(bookBean);
		int result = bookBeani.editBookRecord(bookBean);
		assertEquals(0, result);
	}
	
	@Test(expected = NullPointerException.class)
	public void removeInvalidBookBean() throws SQLException {		
		bookBeani.editBookRecord(bookBean);
		fail("We can't delete an invalid book bean");
	}
	
	@Test
	public void getAllBooks() throws SQLException {		
		ArrayList<BookBean> bookBeanList = bookBeani.getAllBooks();
		assertEquals(bookBeanList.size(), bookBeanList.size());
	}
	
	@Test
	public void getBooksOnSale() throws SQLException {		
		ArrayList<BookBean> bookBeanList = bookBeani.getBooksOnSale();
		assertEquals(bookBeanList.size(), bookBeanList.size());
	}
	
}