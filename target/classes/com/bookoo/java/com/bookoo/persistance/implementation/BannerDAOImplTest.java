package com.bookoo.persistance.implementation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.StringReader;
import java.sql.Connection;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bookoo.data.BannerBean;
import com.bookoo.persistence.implementation.BannerDAOImpl;
import com.bookoo.util.SeedDatabase;


/**
 * @author Sihem, Darrel
 *
 */
@RunWith(Arquillian.class)
public class BannerDAOImplTest {
	
	@Inject
	private BannerBean banner;
	
	@Inject
	private BannerDAOImpl bdi;
	
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
				.addPackage(BannerDAOImpl.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("resources-mysql-ds.xml", "resources.xml")
				//.addAsResource("createBanner.sql")
				.addAsLibraries(dependencies);

		return webArchive;
	}
	
	@Resource(name = "jdbc/g2w14")
	private DataSource ds;
	
	@Ignore
	public void seedDatabase() {
		final String seedDataScript = SeedDatabase.loadAsString("createBanner.sql");
		try (Connection connection = ds.getConnection()) {
			for (String statement : SeedDatabase.splitStatements(new StringReader(
					seedDataScript), ";")) {
				connection.prepareStatement(statement).execute();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed seeding database", e);
		}
	}
	
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() {
//		banner = new BannerBean();
//		bdi = new BannerDAOImpl();
//	}

	@Test
	public void testInsertBanner() throws SQLException {
		
		banner.setImage(".png");
		banner.setDescription("jhg");
		banner.setLink("qsc");
		banner.setWidth(10);
		banner.setHeight(13);
		banner.setAvailable(false);
		
		int results = bdi.insertBanner(banner);

		assertEquals("Expected 1 results to be inserted", 1, results);
		
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testInsertInvalidBannerBean() throws SQLException {
		bdi.insertBanner(banner);
	}
	
	@Test
	public void testUpdateBanner() throws SQLException{
		
		banner.setImage("a.png");
		banner.setDescription("jhag");
		banner.setLink("qsec");
		banner.setWidth(12);
		banner.setHeight(13);
		banner.setAvailable(false);
		
		banner.setId(1);
		
		int results = bdi.updateBanner(banner);

		assertEquals("Expected 1 results to be updated", 1, results);
	}
	
	@Test(expected = NullPointerException.class)
	public void testUpdateInvalidBannerBean() throws SQLException {
		
		bdi.updateBanner(banner);
	}
	
	@Test(expected = SQLException.class)
	public void testUpdateUnexistantBannerBean() throws SQLException {
		banner.setImage("a.png");
		banner.setDescription("jhag");
		banner.setLink("qsec");
		banner.setWidth(12);
		banner.setHeight(13);
		banner.setAvailable(false);
		
		banner.setId(100);
		
		bdi.updateBanner(banner);
	}
	
	@Test
	public void testGetAllBanners() throws SQLException{
		
		ArrayList<BannerBean> results = bdi.getAllBanners();
		
		if(results==null){
			fail("Unexcepted null");
		}
		if(results.isEmpty()){
			fail("Unexcepted empty");
		}
		
	}
	
	@Test
	public void testGetCurrentBanners() throws SQLException{
		
		ArrayList<BannerBean> results = new ArrayList<BannerBean>();
		results = bdi.getCurrentBanners();
		
		if(results==null){
			fail("Unexcepted null");
		}
		if(results.isEmpty()){
			fail("Unexcepted null");
		}
	}
	
	@Test
	public void testGetBannerById() throws SQLException{
		
		//ArrayList<BannerBean> results = new ArrayList<BannerBean>();
		//results = bdi.getBannerById(1);
		
		BannerBean bb = bdi.getBannerById(1);
		
		if(bb==null){
			fail("Banner shouldn't be null");
		}
		if(bb.getId()!=1){
			fail("Wrong values of the ID");
		}
	}

	@Test
	public void testGetBannerByInvalidId() throws SQLException{
		long id = 1000;
		BannerBean bb = bdi.getBannerById(id);
		
		if(bb==null){
			fail("Unexpected null");
		}
		if(Long.valueOf(bb.getId())!=null){
			fail("This id should be null");
		}
	}
}
