package com.bookoo.persistance.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bookoo.data.ClientBean;
import com.bookoo.persistence.implementation.ClientDAOImpl;


/**
 * @author Sihem, Darrel
 *
 */
@RunWith(Arquillian.class)
public class ClientDAOImplTest {
	
	@Deployment
	public static WebArchive deploy() {

		final File[] dependencies = Maven
				.resolver()
				.loadPomFromFile("pom.xml")
				.resolve("mysql:mysql-connector-java",
						"org.assertj:assertj-core").withoutTransitivity()
				.asFile();

		final WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
				.addPackage(ClientBean.class.getPackage())
				.addPackage(ClientDAOImpl.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				//.addAsResource("testDatabase.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Inject
	private ClientDAOImpl cdi;

	@Inject
	private ClientBean clientBean;
	
	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	@Test
	public void testInsertClientBean() throws SQLException {

		clientBean.setUsername("TestUser");
		clientBean.setPassword("12345678");
		clientBean.setManager(false);
		clientBean.setTitle("Mr");
		clientBean.setLastName("Doe");
		clientBean.setFirstName("John");
		clientBean.setCompanyName("Apple Inc");
		clientBean.setAddress1("1234 No Name");
		clientBean.setAddress2("st");
		clientBean.setCity("Montreal");
		clientBean.setCountry("Canada");
		clientBean.setProvince("QC");
		clientBean.setPostalCode("h1s1t5");
		clientBean.setShipTitle("Mr");
		clientBean.setShipLastName("Doe");
		clientBean.setShipFirstName("John");
		clientBean.setShipCompanyName("Apple Inc");
		clientBean.setShipAddress1("1234 No Name");
		clientBean.setShipAddress2("st");
		clientBean.setShipCity("1234 No Name");
		clientBean.setShipProvince("QC");
		clientBean.setShipCountry("Canada");
		clientBean.setShipPostalCode("h1s1w1");
		clientBean.setPhoneNumber("5141231312");
		clientBean.setCellNumber("5141231231");
		clientBean.setEmail("testuser@user.com");
		clientBean.setLastGenre("Classic");

		int results = cdi.insertClientRecord();

		assertEquals("Expected 1 result!", 1, results);
	}


	@Test(expected = SQLException.class)
	public void testInsertInvalidClientBean() throws SQLException {
		cdi.insertClientRecord();
		fail("Can't insert a null bean!");
	}
	
	@Test
	public void testUpdateClientRecord() throws SQLException {
		
		clientBean.setUsername("TestUser");
		clientBean.setPassword("12345678");
		clientBean.setManager(false);
		clientBean.setTitle("Mr");
		clientBean.setLastName("Doe");
		clientBean.setFirstName("John");
		clientBean.setCompanyName("Apple Inc");
		clientBean.setAddress1("1234 No Name");
		clientBean.setAddress2("st");
		clientBean.setCity("Montreal");
		clientBean.setCountry("Canada");
		clientBean.setProvince("QC");
		clientBean.setPostalCode("h1s1t5");
		clientBean.setShipTitle("Mr");
		clientBean.setShipLastName("Doe");
		clientBean.setShipFirstName("John");
		clientBean.setShipCompanyName("Apple Inc");
		clientBean.setShipAddress1("1234 No Name");
		clientBean.setShipAddress2("st");
		clientBean.setShipCity("1234 No Name");
		clientBean.setShipProvince("QC");
		clientBean.setShipCountry("Canada");
		clientBean.setShipPostalCode("h1s1w1");
		clientBean.setPhoneNumber("5141231312");
		clientBean.setCellNumber("5141231231");
		clientBean.setEmail("testuser@user.com");
		clientBean.setLastGenre("Classic");
		clientBean.setId(1);

		int results = cdi.updateUserProfile(clientBean);

		assertEquals("Expected 1 results to be inserted", 1, results);
		
	}
	
	@Test(expected = SQLException.class)
	public void testUpdateInvalidClientBean() throws SQLException {
		cdi.updateUserProfile(new ClientBean());
		fail("Exception was expected but not thrown!");
	}
	
	@Test(expected = SQLException.class)
	public void testUpdateUnexistedClientBean() throws SQLException {
		clientBean.setUsername("Swagster");
		clientBean.setPassword("asdasd");
		clientBean.setManager(false);
		clientBean.setTitle("aasd");
		clientBean.setLastName("Doe");
		clientBean.setFirstName("John");
		clientBean.setCompanyName("Apple Inc");
		clientBean.setAddress1("1234 No Name");
		clientBean.setAddress2("st");
		clientBean.setCity("MTL REAL CITY");
		clientBean.setCountry("Canado lo");
		clientBean.setProvince("PQ");
		clientBean.setPostalCode("h1s1t5");
		clientBean.setShipTitle("asdas");
		clientBean.setShipLastName("asdas");
		clientBean.setShipFirstName("asdasd");
		clientBean.setShipCompanyName("asdasd");
		clientBean.setShipAddress1("asdasd");
		clientBean.setShipAddress2("asdasd");
		clientBean.setShipCity("sdads");
		clientBean.setShipProvince("as");
		clientBean.setShipCountry("asdasd");
		clientBean.setShipPostalCode("h1s1w1");
		clientBean.setPhoneNumber("1231231312");
		clientBean.setCellNumber("5141231231");
		clientBean.setEmail("asdads");
		clientBean.setLastGenre("asdasd");
		
		clientBean.setId(2000);
		
		int results = cdi.updateUserProfile(clientBean);
		assertEquals(0, results);
	}
	
	
	
	@Test
	public void testSearchAllClients() throws SQLException{
		
		ArrayList<ClientBean> test = cdi.getAllClients();
		assertEquals(test, test);
		
	}
		
	@Test(expected = NullPointerException.class)
	public void testSearchInvalidClient() throws Exception {
		cdi.searchClient(new ClientBean().getUsername());
		fail("Exception was expected but not thrown");
	}
	
	@Test(expected = SQLException.class)
	public void testSearchUnexestedClient() throws Exception {
		clientBean.setUsername("username");
		clientBean.setPassword("password");
		clientBean.setManager(false);
		clientBean.setTitle("Mr");
		clientBean.setLastName("Doe");
		clientBean.setFirstName("John");
		clientBean.setCompanyName("Apple Inc");
		clientBean.setAddress1("1234 No Name");
		clientBean.setAddress2("st");
		clientBean.setCity("MTL REAL CITY");
		clientBean.setCountry("Canado lo");
		clientBean.setProvince("PQ");
		clientBean.setPostalCode("h1s1t5");
		clientBean.setShipTitle("asdas");
		clientBean.setShipLastName("asdas");
		clientBean.setShipFirstName("asdasd");
		clientBean.setShipCompanyName("asdasd");
		clientBean.setShipAddress1("asdasd");
		clientBean.setShipAddress2("asdasd");
		clientBean.setShipCity("sdads");
		clientBean.setShipProvince("QC");
		clientBean.setShipCountry("asdasd");
		clientBean.setShipPostalCode("h1s1w1");
		clientBean.setPhoneNumber("1231231312");
		clientBean.setCellNumber("5141231231");
		clientBean.setEmail("asdads");
		clientBean.setLastGenre("asdasd");
		
		clientBean.setId(1000);
		
		ClientBean c = cdi.searchClient(clientBean.getUsername());
		assertEquals(0, c.getId());

	}
	
}
