package com.bookoo.persistence.implementation;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.bookoo.data.BannerBean;
import com.bookoo.data.FileUploadBean;
import com.bookoo.persistence.interfaces.BannersDAO;

/**
 * Data Access routine implementation for the Client Data Bean
 * 
 * @author Jolan Cornevin, Sihem Adnani
 * @version 1.0
 */

@Named("bannerAction")
@RequestScoped
public class BannerDAOImpl implements BannersDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7254132481136028271L;
	private ArrayList<BannerBean> currentBannerList;

	public ArrayList<BannerBean> getCurrentBannerList() {
		return currentBannerList;
	}

	public void setCurrentBannerList(ArrayList<BannerBean> currentBannerList) {
		this.currentBannerList = currentBannerList;
	}

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	@Inject
	BannerBean banner;
	@Inject
	FileUploadBean file;

	public BannerDAOImpl() {
		super();
		currentBannerList = new ArrayList<BannerBean>();
	}

	/*
	 * Insert a banner in the table BANNERS_ADS
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.BannersDAO#insertBanner(com.bookoo.
	 * data.BannerBean)
	 */
	@Override
	public int insertBanner(BannerBean banner) throws SQLException {

		String preparedQuery = "INSERT INTO BANNERS_ADS (AD_IMAGE, DESCRIPTION, AD_LINK, AVAILABLE) values (?, ?, ?, ?, ?, ?)";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {
			ps.setString(1, banner.getImage());
			ps.setString(2, banner.getDescription());
			ps.setString(3, banner.getLink());
			ps.setBoolean(6, banner.isAvailable());

			return ps.executeUpdate();

		}

	}

	/*
	 * Update all values of the banner give in parameter
	 * 
	 * @see
	 * com.bookoo.persistence.interfaces.BannersDAO#updateBanner(com.bookoo.
	 * data.BannerBean)
	 */
	@Override
	public int updateBanner(BannerBean updateBanner) throws SQLException {
		String preparedQuery = "UPDATE BANNERS_ADS SET AD_IMAGE = ?, DESCRIPTION = ?, AD_LINK = ?, AVAILABLE = ? WHERE AD_ID = ?";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setString(1, updateBanner.getImage());
			ps.setString(2, updateBanner.getDescription());
			ps.setString(3, updateBanner.getLink());
			ps.setBoolean(4, updateBanner.isAvailable());
			ps.setLong(5, updateBanner.getId());
			return ps.executeUpdate();

		}
	}

	@Override
	public int updateBannerBean(int id) throws SQLException {

		String preparedQuery = "UPDATE BANNERS_ADS SET AD_IMAGE = ?, DESCRIPTION = ?, AD_LINK = ?, AVAILABLE = ? WHERE AD_ID = ?";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setString(1, banner.getImage());
			ps.setString(2, banner.getDescription());
			ps.setString(3, banner.getLink());
			ps.setBoolean(4, banner.isAvailable());
			ps.setLong(5, id);
			return ps.executeUpdate();

		}
	}

	/*
	 * get the banner with the id given in parameter
	 * 
	 * @see com.bookoo.persistence.interfaces.BannersDAO#getBannerById(long)
	 */
	public BannerBean getBannerById(long id) throws SQLException {
		BannerBean banner = new BannerBean();

		String preparedQuery = "SELECT * from BANNERS_ADS where AD_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setLong(1, id);

			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					// Process results

					banner.setId(resultSet.getLong("AD_ID"));
					banner.setImage(resultSet.getString("AD_IMAGE"));
					banner.setDescription(resultSet.getString("DESCRIPTION"));
					banner.setLink(resultSet.getString("AD_LINK"));
					banner.setAvailable(resultSet.getBoolean("AVAILABLE"));

				}
			}

			return banner;
		}

	}

	/*
	 * get all banners
	 * 
	 * @see com.bookoo.persistence.interfaces.BannersDAO#getAllBanners()
	 */
	@Override
	public ArrayList<BannerBean> getAllBanners() throws SQLException {

		ArrayList<BannerBean> results = new ArrayList<BannerBean>();

		String selectSQL = "SELECT * FROM BANNERS_ADS";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();

				PreparedStatement pStatement = connection
						.prepareStatement(selectSQL);
				ResultSet resultSet = pStatement.executeQuery()) {
			while (resultSet.next()) {

				// Process results
				BannerBean banner = new BannerBean();
				banner.setId(resultSet.getLong("AD_ID"));
				banner.setImage(resultSet.getString("AD_IMAGE"));
				banner.setDescription(resultSet.getString("DESCRIPTION"));
				banner.setLink(resultSet.getString("AD_LINK"));
				banner.setAvailable(resultSet.getBoolean("AVAILABLE"));

				results.add(banner);
			}
		}
		return results;
	}

	/*
	 * Get all banners where AVAILABLE is true
	 * 
	 * @see com.bookoo.persistence.interfaces.BannersDAO#getCurrentBanners()
	 */
	@Override
	public ArrayList<BannerBean> getCurrentBanners() throws SQLException {
		ArrayList<BannerBean> results = new ArrayList<BannerBean>();

		String selectSQL = "SELECT * FROM BANNERS_ADS WHERE AVAILABLE = TRUE";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();

				PreparedStatement pStatement = connection
						.prepareStatement(selectSQL);
				ResultSet resultSet = pStatement.executeQuery()) {
			while (resultSet.next()) {

				// Process results
				BannerBean banner = new BannerBean();
				banner.setId(resultSet.getLong("AD_ID"));
				banner.setImage(resultSet.getString("AD_IMAGE"));
				banner.setDescription(resultSet.getString("DESCRIPTION"));
				banner.setLink(resultSet.getString("AD_LINK"));
				banner.setAvailable(resultSet.getBoolean("AVAILABLE"));

				results.add(banner);
			}
		}
		Collections.shuffle(results);
		currentBannerList.addAll(results);
		return currentBannerList;
	}

	/**
	 * Sets left bottom ad banner
	 */
	public BannerBean getLeftCurrentBanner() throws SQLException {

		if (currentBannerList != null && currentBannerList.size() > 1)
			return currentBannerList.get(0);
		else
			return null;
	}

	/**
	 * Sets right bottom ad banner
	 */
	public BannerBean getRightCurrentBanner() throws SQLException {
		if (currentBannerList != null && currentBannerList.size() > 2)
			return currentBannerList.get(1);
		else
			return null;
	}

	public String insertBa() throws SQLException {
		banner.setImage(file.getNameFile());
		insertBanner(banner);
		return null;
	}

}
