/**
 * 
 */
package com.bookoo.persistance.implementation;

import static org.junit.Assert.*;

import java.io.File;
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
import com.bookoo.data.SurveyBean;
import com.bookoo.persistence.implementation.SurveyDAOImpl;

/**
 * @author Sihem
 *
 */
@RunWith(Arquillian.class)
public class SurveyDAOImplTest {
	
	@Inject
	private SurveyDAOImpl sdi;
	
	@Inject
	private SurveyBean survey;
	
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
				.addPackage(SurveyDAOImpl.class.getPackage())
				.addPackage(SurveyBean.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				//.addAsResource("createSurvey.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Before
	public void setUp(){
		survey = new SurveyBean();
		sdi = new SurveyDAOImpl();
	}
	
	/**
	 * Test method for {@link com.bookoo.persistence.implementation.SurveyDAOImpl#insertSurvey(com.bookoo.data.SurveyBean)}.
	 * @throws SQLException 
	 */
	@Test
	public void testInsertSurvey() throws SQLException {
		
		survey.setQuestion("jghj");
		survey.setOption1("a");
		survey.setOption2("b");
		survey.setOption3("c");
		survey.setOption4("d");
		survey.setOption5("e");
		survey.setResutl1(1);
		survey.setResutl2(2);
		survey.setResutl3(3);
		survey.setResutl4(4);
		survey.setResutl5(5);
		survey.setAvailable(true);
		
		int results = sdi.insertSurvey(survey);
		assertEquals("Expected 1 results to be inserted", 1, results);
	}
	

	@Test(expected = SQLException.class)
	public void testInsertInvalidSurvey() throws SQLException{
		sdi.insertSurvey(survey);
		fail("Can't insert a null bean");
	}

	/**
	 * Test method for {@link com.bookoo.persistence.implementation.SurveyDAOImpl#updateSurvey(com.bookoo.data.SurveyBean)}.
	 * @throws SQLException 
	 */
	@Test
	public void testUpdateSurvey() throws SQLException {
		
		survey.setQuestion("traeio?");
		survey.setOption1("a");
		survey.setOption2("b");
		survey.setOption3("c");
		survey.setOption4("d");
		survey.setOption5("e");
		survey.setResutl1(1);
		survey.setResutl2(2);
		survey.setResutl3(3);
		survey.setResutl4(4);
		survey.setResutl5(5);
		survey.setAvailable(true);
		survey.setSurveyId(1);
		
		int results = sdi.updateSurvey(survey);
		assertEquals("Expected 1 results to be inserted", 1, results);
	}
	
	@Test
	public void testUpdateInvalidReview() throws SQLException{
		int results = sdi.updateSurvey(survey);
		assertEquals(0, results);
		
	}

	/**
	 * Test method for {@link com.bookoo.persistence.implementation.SurveyDAOImpl#getAllSurveys()}.
	 * @throws SQLException 
	 */
	@Test
	public void testGetAllSurveys() throws SQLException {
	ArrayList<SurveyBean> test = sdi.getAllSurveys();
		
		if(test==null){
			fail("Unexcepted null");
		}
	}

	/**
	 * Test method for {@link com.bookoo.persistence.implementation.SurveyDAOImpl#getCurrentSurvey()}.
	 * @throws SQLException 
	 */
	@Test
	public void testGetCurrentSurvey() throws SQLException {
		
		ArrayList<SurveyBean> test = sdi.getCurrentSurvey();
		
		if(test==null){
			fail("Unexcepted null");
		}
	}

	/**
	 * Test method for {@link com.bookoo.persistence.implementation.SurveyDAOImpl#getSurveyById(long)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetSurveyById() throws Exception {
		
		long id = 1;
		ArrayList<SurveyBean> test = sdi.getSurveyById(id);
		
		if(test==null){
			throw new Exception("Unexcepted null");
		}
	}
	
	@Test
	public void getSurveyByNonexistentId() throws Exception {
		long lg= 1000;
		
		ArrayList<SurveyBean> test = sdi.getSurveyById(lg);
		
		if(test==null){
			throw new Exception("Unexcepted null");
		}
	}

}
