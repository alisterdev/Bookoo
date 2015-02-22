package com.bookoo.persistance.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;

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

import com.bookoo.data.ReviewBean;
import com.bookoo.persistence.implementation.ReviewDAOImpl;

/**
 * Test for ReviewDAOImpl
 * 
 * @author Sihem, Darrel-Day 
 * @version 1.0
 */

@RunWith(Arquillian.class)
public class ReviewDAOImplTest {
	
	@Inject
	private ReviewDAOImpl rdi;

	@Inject
	private ReviewBean review;
	
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
				.addPackage(ReviewDAOImpl.class.getPackage())
				.addPackage(ReviewBean.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				//.addAsResource("createReviews.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Before 
	public void setUp() throws SQLException{
		review = new ReviewBean();
		rdi = new ReviewDAOImpl();
	}
	
	
	@Test
	public void testInsertReview() throws SQLException{
		
		java.util.Date date= new java.util.Date();
		
		review.setBookId(1);
		review.setDate(new Timestamp(date.getTime()));
		review.setClientId(2);
		review.setUsername("adff");
		review.setRating(3);
		review.setReview("This book was good");
		review.setApprovalStatus(true);
		
		int results = rdi.insertReview(review);
		assertEquals("Expected 1 results to be inserted", 1, results);
	}
	
	@Test(expected = NullPointerException.class)
	public void testInsertInvalidReview() throws SQLException{
		rdi.insertReview(review);
		fail("Can't insert a null bean");
	}
	
	@Test
	public void testUpdateReview() throws SQLException{
		
		java.util.Date date= new java.util.Date();
		
		review.setId(5);
		review.setBookId(1);
		review.setDate(new Timestamp(date.getTime()));
		review.setClientId(2);
		review.setUsername("FGH");
		review.setRating(4);
		review.setReview("This book was not good");
		review.setApprovalStatus(true);
		
		int results = rdi.updateReview(review);
		assertEquals("Expected 1 results to be updated", 1, results);
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testUpdateInvalidReview() throws SQLException{
		rdi.updateReview(review);
		fail("Can't update a null ReviewBean");
	}
	
	@Test
	public void testRemoveReview() throws SQLException{
		review.setId(5);
		
		int results = rdi.removeReview(review);
		assertEquals("Expected 1 results to be removed", 1, results);	
	}
	
	@Test
	public void testRemoveInvalidReview() throws SQLException{
		int results = rdi.removeReview(review);
		assertEquals(0, results);
	}
	
	
}
