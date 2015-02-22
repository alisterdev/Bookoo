package com.bookoo.persistance.implementation;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.File;
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
import com.bookoo.data.NewsFeedBean;
import com.bookoo.persistence.implementation.NewsFeedDAOImpl;
/**
 * Test for NewsFeedDAOImpl
 * 
 * @author Jolan Cornevin
 * @version 1.0
 */
@RunWith(Arquillian.class)
public class NewsFeedDAOImplTest {
	@Inject
	private NewsFeedDAOImpl nbi;

	@Inject
	private NewsFeedBean newsFeed;
	
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
				.addPackage(NewsFeedBean.class.getPackage())
				.addPackage(NewsFeedDAOImpl.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				//.addAsResource("createNewsFeed.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Before
	public void setUp() {
		newsFeed = new NewsFeedBean();
		nbi = new NewsFeedDAOImpl();
	}
	
	private NewsFeedBean fillNews(NewsFeedBean nbT){
		java.util.Date date= new java.util.Date();
		
		nbT.setTitle("title");
		nbT.setLink("link");
		nbT.setDate(new Timestamp(date.getTime()));
		nbT.setImage("image");
		nbT.setAvailable(true);
		
		return nbT;
	}
	
	@Test(expected = SQLException.class)
	public void insertInvalidNewsFeedBean() throws SQLException {
		nbi.insertNewsFeed(newsFeed);
		fail("Can't insert an null NewsFeedBean");
	}
	
	@Test
	public void insertNewsFeedBean() throws SQLException {
		java.util.Date date= new java.util.Date();
		
		newsFeed.setTitle("title");
		newsFeed.setLink("link");
		newsFeed.setDate(new Timestamp(date.getTime()));
		newsFeed.setImage("image");
		newsFeed.setAvailable(true);
		
		int results = nbi.insertNewsFeed(newsFeed);
		assertEquals("Expected 1 results to be inserted", 1, results);
	}
	
	@Test(expected = SQLException.class)
	public void updateInvalidNewsFeedBean() throws SQLException {
		nbi.insertNewsFeed(newsFeed);
		fail("Can't update an null NewsFeedBean");
	}
	
	@Test(expected = SQLException.class)
	public void updateNonexistentNewsFeedBean() throws SQLException {
		newsFeed.setId(1000);
		
		int result = nbi.insertNewsFeed(newsFeed);
		
		assertEquals(0, result);
	}

	@Test
	public void updateNewsFeedBean() throws SQLException {
		java.util.Date date= new java.util.Date();
		
		newsFeed.setTitle("title*");
		newsFeed.setLink("link*");
		newsFeed.setDate(new Timestamp(date.getTime()));
		newsFeed.setImage("image*");
		newsFeed.setAvailable(true);
		
		int results = nbi.updateNewsFeed(newsFeed);
		assertEquals(1, results);
	}
	
	@Test(expected = SQLException.class)
	public void removeInvalidNewsFeedBean() throws SQLException {
		nbi.removeNewsFeed(newsFeed);
		fail("Can't remove an null NewsFeedBean");
	}
	
	@Test
	public void removeNewsFeedBean() throws SQLException {
		newsFeed.setId(1);
		newsFeed = fillNews(newsFeed);
		
		int result = nbi.removeNewsFeed(newsFeed);
		assertEquals(1, result);
	}
	
	@Test(expected = SQLException.class)
	public void removeNonexistentNewsFeedBean() throws SQLException {
		newsFeed.setId(1);
		
		int result = nbi.removeNewsFeed(newsFeed);
		assertEquals(0, result);
	}
	
	@Test
	public void getAllNewsFeeds() throws SQLException{
		ArrayList<NewsFeedBean> test = nbi.getAllNewsFeeds();
		if(test==null) System.out.println("Shouldn't be null");
	}
	
	@Test
	public void getCurrentNewsFeed() throws SQLException{
		ArrayList<NewsFeedBean> test = nbi.getCurrentNewsFeed();
		if(test==null) fail("Shouldn't be null");
		if(test.isEmpty()) fail("Shouldn't be empty");
	}
	
	@Test
	public void getNewsFeedById() throws SQLException{
		long id=1;
		
		NewsFeedBean test = nbi.getNewsFeedById(id);
		
		if(test==null) fail("Shouldn't be null");
	}
	
	@Test
	public void getNonexistentNewsFeedById() throws SQLException{
		long id=1000;
		
		NewsFeedBean test = nbi.getNewsFeedById(id);
		
		if(test==null) fail("Shouldn't be null");
		
		assertThat(test.getId(), not(null) );
	}
	
}
