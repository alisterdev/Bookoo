package com.bookoo.persistance.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.SQLException;

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

import com.bookoo.data.RSSBean;
import com.bookoo.persistence.implementation.RSSDAOImpl;

/**
 * Test for RSSDAOImpl
 * 
 * @author Jolan Cornevin
 * @version 1.0
 */
@RunWith(Arquillian.class)
public class NewsFeedDAOImplTest {
	@Inject
	private RSSDAOImpl nbi;

	@Inject
	private RSSBean RSS;

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
				.addPackage(RSSBean.class.getPackage())
				.addPackage(RSSDAOImpl.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				// .addAsResource("createRSS.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}

	@Before
	public void setUp() {
		RSS = new RSSBean();
		nbi = new RSSDAOImpl();
	}

	private RSSBean fillNews(RSSBean nbT) {

		nbT.setTitle("title");
		nbT.setLink("link");
		nbT.setAvailable(true);

		return nbT;
	}

	@Test(expected = SQLException.class)
	public void insertInvalidRSSBean() throws SQLException {
		nbi.insertRSS();
		fail("Exception is expected but not thrown!");
	}

	@Test
	public void insertRSSBean() throws SQLException {

		RSS.setTitle("title");
		RSS.setLink("link");
		RSS.setAvailable(true);

		int results = nbi.insertRSS();
		assertEquals("Expected 1 results to be inserted", 1, results);
	}

	@Test(expected = SQLException.class)
	public void updateInvalidRSSBean() throws SQLException {
		nbi.insertRSS();
		fail("Can't update an null RSSBean");
	}

	@Test(expected = SQLException.class)
	public void updateNonexistentRSSBean() throws SQLException {
		RSS.setId(1000);

		int result = nbi.insertRSS();

		assertEquals(0, result);
	}

	@Test
	public void updateRSSBean() throws SQLException {

		RSS.setTitle("title*");
		RSS.setLink("link*");
		RSS.setAvailable(true);

		int results = nbi.updateRSS(RSS);
		assertEquals(1, results);
	}

	@Test(expected = SQLException.class)
	public void removeInvalidRSSBean() throws SQLException {
		nbi.removeRSS();
		fail("Can't remove an null RSSBean");
	}

	@Test
	public void removeRSSBean() throws SQLException {
		RSS.setId(1);
		RSS = fillNews(RSS);

		int result = nbi.removeRSS();
		assertEquals(1, result);
	}

	@Test(expected = SQLException.class)
	public void removeNonexistentRSSBean() throws SQLException {
		RSS.setId(1);

		int result = nbi.removeRSS();
		assertEquals(0, result);
	}

}
